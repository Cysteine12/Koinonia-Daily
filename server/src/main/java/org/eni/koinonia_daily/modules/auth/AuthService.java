package org.eni.koinonia_daily.modules.auth;

import org.eni.koinonia_daily.modules.auth.dto.LoginRequest;
import org.eni.koinonia_daily.modules.auth.dto.LoginResponse;
import org.eni.koinonia_daily.modules.auth.dto.RegisterRequest;
import org.eni.koinonia_daily.modules.auth.dto.UserProfileDto;
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
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final EmailService emailService;

  public void register(RegisterRequest payload) {
    if (userRepository.existsByEmail(payload.getEmail()))  {
      throw new IllegalArgumentException("This email already exists");
    }

    User user = User.builder()
                  .firstName(payload.getFirstName())
                  .lastName(payload.getLastName())
                  .email(payload.getEmail())
                  .password(passwordEncoder.encode(payload.getPassword()))
                  .build();
    
    userRepository.save(user);
  }
  
  public LoginResponse login(LoginRequest payload) {
    Authentication authentication = authenticationManager
                                      .authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));
    
    String accessToken = jwtService.generateToken(authentication.getName(), 7 * 24 * 60 * 60 * 1000);
    String refreshToken = jwtService.generateToken(authentication.getName(), 31 * 24 * 60 * 60 * 1000);
                                      
    return LoginResponse.builder()
            .message("Login Successful")
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
            .build();
  }
}
