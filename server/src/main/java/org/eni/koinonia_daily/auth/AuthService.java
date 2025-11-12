package org.eni.koinonia_daily.auth;

import org.eni.koinonia_daily.auth.dto.LoginRequest;
import org.eni.koinonia_daily.auth.dto.LoginResponse;
import org.eni.koinonia_daily.auth.dto.RegisterRequest;
import org.eni.koinonia_daily.auth.dto.UserProfileDto;
import org.eni.koinonia_daily.user.User;
import org.eni.koinonia_daily.user.UserRepository;
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

  public void register(RegisterRequest payload) {
    if (userRepository.existsByEmail(payload.getEmail()))  {
      throw new IllegalArgumentException("This email already exists");
    }

    User user = new User();
    user.setFirstName(payload.getFirstName());
    user.setLastName(payload.getLastName());
    user.setEmail(payload.getEmail());
    user.setPassword(passwordEncoder.encode(payload.getPassword()));
    
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
