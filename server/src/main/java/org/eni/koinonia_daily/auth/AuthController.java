package org.eni.koinonia_daily.auth;

import org.eni.koinonia_daily.auth.dto.LoginRequest;
import org.eni.koinonia_daily.auth.dto.LoginResponse;
import org.eni.koinonia_daily.auth.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest payload) {
    authService.register(payload);

    return ResponseEntity.ok("Registration successful");
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest payload) {
    String token = authService.login(payload);

    return ResponseEntity.ok(
      LoginResponse
        .builder()
        .message("Login Successful")
        .token(token)
        .build()
    );
  }
}
