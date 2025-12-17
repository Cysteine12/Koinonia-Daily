package org.eni.koinonia_daily.modules.auth;

import java.time.Duration;
import java.time.LocalDateTime;

import org.eni.koinonia_daily.exceptions.NotFoundException;
import org.eni.koinonia_daily.exceptions.UnauthorizedException;
import org.eni.koinonia_daily.exceptions.ValidationException;
import org.eni.koinonia_daily.modules.auth.dto.ChangePasswordDto;
import org.eni.koinonia_daily.modules.auth.dto.ForgotPasswordDto;
import org.eni.koinonia_daily.modules.auth.dto.LoginRequest;
import org.eni.koinonia_daily.modules.auth.dto.LoginResponse;
import org.eni.koinonia_daily.modules.auth.dto.RefreshTokenRequest;
import org.eni.koinonia_daily.modules.auth.dto.RefreshTokenResponse;
import org.eni.koinonia_daily.modules.auth.dto.RegisterRequest;
import org.eni.koinonia_daily.modules.auth.dto.RequestOtpDto;
import org.eni.koinonia_daily.modules.auth.dto.ResetPasswordDto;
import org.eni.koinonia_daily.modules.auth.dto.UserProfileDto;
import org.eni.koinonia_daily.modules.auth.dto.VerifyEmailDto;
import org.eni.koinonia_daily.modules.token.TokenService;
import org.eni.koinonia_daily.modules.token.TokenType;
import org.eni.koinonia_daily.services.EmailService;
import org.eni.koinonia_daily.modules.user.User;
import org.eni.koinonia_daily.modules.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final EmailService emailService;
  private static final long ACCESS_TOKEN_EXPIRATION_MS = Duration.ofDays(7).toMillis();
  private static final long REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(31).toMillis();

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

    emailService.sendEmailVerificationRequestMail(user.getEmail(), user.getFirstName(), otp);

    return;
  }
  
  public LoginResponse login(LoginRequest payload) {

    Authentication auth = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));

    UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
    if (!principal.isVerified()) throw new UnauthorizedException("Email verification required");
    
    String accessToken = jwtService.generateToken(auth.getName(), ACCESS_TOKEN_EXPIRATION_MS, TokenType.ACCESS_TOKEN);
    String refreshToken = jwtService.generateToken(auth.getName(), REFRESH_TOKEN_EXPIRATION_MS, TokenType.REFRESH_TOKEN);

    tokenService.create(auth.getName(), refreshToken, TokenType.REFRESH_TOKEN, LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_MS / 1000));
                                      
    return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  public UserProfileDto profile(UserPrincipal user) {

    return UserProfileDto.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
  }

	public void verifyEmail(VerifyEmailDto payload) {

		User user = userRepository.findByEmail(payload.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));
      
    if (user.isVerified()) {
      throw new ValidationException("User is already verified");
    }
    
    tokenService.verifyEmailOtp(user, payload.getOtp());

    user.setVerified(true);
    userRepository.save(user);

    emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

    return;
	}

	public void requestOtp(RequestOtpDto payload) {
    
		User user = userRepository.findByEmail(payload.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));
        
    if (user.isVerified()) {
      throw new ValidationException("User is already verified");
    }

    String otp = tokenService.generateAndSaveOtp(user.getEmail(), TokenType.VERIFY_EMAIL);

    emailService.sendEmailVerificationRequestMail(user.getEmail(), user.getFirstName(), otp);

    return;
	}

  public void forgotPassword(ForgotPasswordDto payload) {
    
    User user = userRepository.findByEmail(payload.getEmail())
                  .orElseThrow(() -> new NotFoundException("User not found"));

    String otp = tokenService.generateAndSaveOtp(user.getEmail(), TokenType.CHANGE_PASSWORD);

    emailService.sendForgotPasswordMail(user.getEmail(), otp);

    return;
  }

  public void resetPassword(ResetPasswordDto payload) {
    
    tokenService.verifyPasswordOtp(payload.getEmail(), payload.getOtp());

    String newPassword = passwordEncoder.encode(payload.getPassword());

    User newUser = userRepository.findByEmail(payload.getEmail())
                    .map(user -> {
                      user.setPassword(newPassword);
                      return userRepository.save(user);
                    })
                    .orElseThrow(() -> new NotFoundException("User not found"));

    emailService.sendPasswordChangedMail(newUser.getEmail(), newUser.getFirstName());
                
    return;
  }

  public void changePassword(UserPrincipal user, ChangePasswordDto payload) {

    User currentUser = userRepository.findById(user.getId())
                        .orElseThrow(() -> new NotFoundException("User not found"));

    boolean isMatch = passwordEncoder.matches(payload.getCurrentPassword(), currentUser.getPassword());
    if (!isMatch) throw new UnauthorizedException("Incorrect password");

    String newPassword = passwordEncoder.encode(payload.getNewPassword());
    currentUser.setPassword(newPassword);

    userRepository.save(currentUser);

    emailService.sendPasswordChangedMail(currentUser.getEmail(), currentUser.getFirstName());
    
    return;
  }

  public RefreshTokenResponse refreshToken(RefreshTokenRequest payload) {

    String email = jwtService.validateAndExtractSubject(payload.getRefreshToken(), TokenType.REFRESH_TOKEN);

    tokenService.verifyRefreshToken(email, payload.getRefreshToken());

    String accessToken = jwtService.generateToken(email, ACCESS_TOKEN_EXPIRATION_MS, TokenType.ACCESS_TOKEN);
    String refreshToken = jwtService.generateToken(email, REFRESH_TOKEN_EXPIRATION_MS, TokenType.REFRESH_TOKEN);

    tokenService.create(email, refreshToken, TokenType.REFRESH_TOKEN, LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_MS / 1000));

    return RefreshTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }
}
