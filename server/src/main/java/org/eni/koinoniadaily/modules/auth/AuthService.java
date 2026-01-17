package org.eni.koinoniadaily.modules.auth;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.modules.auth.dto.ChangePasswordDto;
import org.eni.koinoniadaily.modules.auth.dto.ForgotPasswordDto;
import org.eni.koinoniadaily.modules.auth.dto.JwtPayload;
import org.eni.koinoniadaily.modules.auth.dto.LoginRequest;
import org.eni.koinoniadaily.modules.auth.dto.LoginResponse;
import org.eni.koinoniadaily.modules.auth.dto.RefreshTokenRequest;
import org.eni.koinoniadaily.modules.auth.dto.RefreshTokenResponse;
import org.eni.koinoniadaily.modules.auth.dto.RegisterRequest;
import org.eni.koinoniadaily.modules.auth.dto.RequestOtpDto;
import org.eni.koinoniadaily.modules.auth.dto.ResetPasswordDto;
import org.eni.koinoniadaily.modules.auth.dto.TokenPair;
import org.eni.koinoniadaily.modules.auth.dto.VerifyEmailDto;
import org.eni.koinoniadaily.modules.auth.events.EmailVerificationRequestedEvent;
import org.eni.koinoniadaily.modules.auth.events.PasswordChangedEvent;
import org.eni.koinoniadaily.modules.auth.events.PasswordResetOtpGeneratedEvent;
import org.eni.koinoniadaily.modules.auth.events.UserRegisteredEvent;
import org.eni.koinoniadaily.modules.token.TokenService;
import org.eni.koinoniadaily.modules.token.TokenType;
import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final CurrentUserProvider currentUserProvider;
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final ApplicationEventPublisher publisher;

  @Transactional
  public void register(RegisterRequest payload) {

    if (userRepository.existsByEmail(payload.getEmail()))  {
      throw new ValidationException("This email already exists");
    }

    User user = User.builder()
                  .firstName(payload.getFirstName())
                  .lastName(payload.getLastName())
                  .email(payload.getEmail())
                  .password(passwordEncoder.encode(payload.getPassword()))
                  .build();
    
    userRepository.save(user);

    String otp = tokenService.generateAndSaveOtp(user.getEmail(), TokenType.VERIFY_EMAIL);

    publisher.publishEvent(new EmailVerificationRequestedEvent(user.getEmail(), user.getFirstName(), otp));
  }
  
  /**
   * Authenticates the given credentials and issues a new pair of access and refresh tokens.
   *
   * The method verifies that the authenticated user's email is confirmed before issuing tokens.
   *
   * @param payload the login request containing the user's email and password
   * @return a LoginResponse containing the issued access token and refresh token
   * @throws UnauthorizedException if the authenticated user's email is not verified
   */
  @Transactional
  public LoginResponse login(LoginRequest payload) {

    Authentication auth = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));

    UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
    if (!principal.isVerified()) {
      throw new UnauthorizedException("Email verification required");
    }

    TokenPair tokens = tokenService.generateAndSaveTokens(auth.getName());
    
    return LoginResponse.builder()
            .accessToken(tokens.getAccessToken())
            .refreshToken(tokens.getRefreshToken())
            .build();
  }

  /**
   * Verifies a user's email by consuming the provided OTP and marking the account as verified.
   *
   * @param request contains the user's email and the one-time password (OTP) to verify
   * @throws NotFoundException   if no user exists for the provided email
   * @throws ValidationException if the user is already verified
   */
  @Transactional
  public void verifyEmail(VerifyEmailDto request) {

		User user = userRepository.findByEmail(request.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));
      
    if (user.isVerified()) {
      throw new ValidationException("User is already verified");
    }
    
    tokenService.consumeEmailOtp(user.getEmail(), request.getOtp());

    user.setVerified(true);

    publisher.publishEvent(new UserRegisteredEvent(user.getEmail(), user.getFirstName()));
  }

  /**
   * Generates a verification one-time password (OTP) for the given email and publishes an email verification event.
   *
   * @param request the request containing the user's email to which the OTP will be sent
   * @throws NotFoundException if no user exists with the provided email
   * @throws ValidationException if the user has already been verified
   */
  @Transactional
  public void requestOtp(RequestOtpDto request) {
    
		User user = userRepository.findByEmail(request.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));
        
    if (user.isVerified()) {
      throw new ValidationException("User is already verified");
    }

    String otp = tokenService.generateAndSaveOtp(user.getEmail(), TokenType.VERIFY_EMAIL);

    publisher.publishEvent(new EmailVerificationRequestedEvent(user.getEmail(), user.getFirstName(), otp));
  }

  /**
   * Generates a one-time password for changing the user's password and publishes an event containing the OTP.
   *
   * @param request the DTO containing the user's email to receive the password reset OTP
   * @throws NotFoundException if no user exists with the provided email
   */
  @Transactional
  public void forgotPassword(ForgotPasswordDto request) {
    
    User user = userRepository.findByEmail(request.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));

    String otp = tokenService.generateAndSaveOtp(user.getEmail(), TokenType.CHANGE_PASSWORD);

    publisher.publishEvent(new PasswordResetOtpGeneratedEvent(user.getEmail(), otp));
  }

  @Transactional
  public void resetPassword(ResetPasswordDto request) {
    
    User user = userRepository.findByEmail(request.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));

    tokenService.consumePasswordOtp(request.getEmail(), request.getOtp());

    String newPassword = passwordEncoder.encode(request.getPassword());

    user.setPassword(newPassword);

    tokenService.revokeAllTokens(user.getEmail(), TokenType.REFRESH_TOKEN);

    publisher.publishEvent(new PasswordChangedEvent(user.getEmail(), user.getFirstName()));
  }

  @Transactional
  public void changePassword(ChangePasswordDto request) {

    UserPrincipal currentUser = currentUserProvider.getCurrentUser();

    User user = userRepository.findById(currentUser.getId())
                  .orElseThrow(() -> new NotFoundException("User not found"));

    boolean isMatch = passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());
    if (!isMatch) {
      throw new UnauthorizedException("Incorrect password");
    }

    String newPassword = passwordEncoder.encode(request.getNewPassword());
    
    user.setPassword(newPassword);

    tokenService.revokeAllTokens(user.getEmail(), TokenType.REFRESH_TOKEN);

    publisher.publishEvent(new PasswordChangedEvent(user.getEmail(), user.getFirstName()));
  }

  @Transactional
  public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

    JwtPayload jwtPayload = jwtService.validateAndExtractPayload(request.getRefreshToken(), TokenType.REFRESH_TOKEN);

    tokenService.consumeRefreshToken(jwtPayload.getSubject(), jwtPayload.getJti());

    TokenPair tokens = tokenService.generateAndSaveTokens(jwtPayload.getSubject());
    
    return RefreshTokenResponse.builder()
            .accessToken(tokens.getAccessToken())
            .refreshToken(tokens.getRefreshToken())
            .build();
  }

  @Transactional
  public void logout(RefreshTokenRequest request) {
    
    JwtPayload jwtPayload = jwtService.validateAndExtractPayload(request.getRefreshToken(), TokenType.REFRESH_TOKEN);

    tokenService.consumeRefreshToken(jwtPayload.getSubject(), jwtPayload.getJti());
  }
}