package org.eni.koinonia_daily.modules.auth;

import org.eni.koinonia_daily.modules.auth.dto.LoginRequest;
import org.eni.koinonia_daily.modules.auth.dto.LoginResponse;
import org.eni.koinonia_daily.modules.auth.dto.RegisterRequest;
import org.eni.koinonia_daily.modules.auth.dto.RegisterResponse;
import org.eni.koinonia_daily.modules.auth.dto.UserProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest payload) {
    RegisterResponse res = authService.register(payload);

    return ResponseEntity.ok(res);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest payload) {
    LoginResponse res = authService.login(payload);

    return ResponseEntity.ok(res);
  }

  @GetMapping("/profile")
  public ResponseEntity<UserProfileDto> profile(@AuthenticationPrincipal UserPrincipal user) {
    UserProfileDto res = authService.profile(user);

    return ResponseEntity.ok(res);
  }
}
