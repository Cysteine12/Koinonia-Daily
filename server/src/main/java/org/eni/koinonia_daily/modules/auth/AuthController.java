package org.eni.koinonia_daily.modules.auth;

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
import org.eni.koinonia_daily.utils.SuccessResponse;
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
  public ResponseEntity<SuccessResponse<Void>> register(@RequestBody @Valid RegisterRequest payload) {

    authService.register(payload);

    return ResponseEntity.ok(SuccessResponse.message("Registration successful. OTP has been sent to your email for verification."));
  }

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest payload) {
    
    LoginResponse res = authService.login(payload);

    return ResponseEntity.ok(SuccessResponse.of(res, "Login Successful"));
  }

  @GetMapping("/profile")
  public ResponseEntity<SuccessResponse<UserProfileDto>> profile(@AuthenticationPrincipal UserPrincipal user) {
    
    UserProfileDto res = authService.profile(user);

    return ResponseEntity.ok(SuccessResponse.data(res));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<SuccessResponse<Void>> verifyEmail(@RequestBody @Valid VerifyEmailDto payload) {
    
    authService.verifyEmail(payload);

    return ResponseEntity.ok(SuccessResponse.message("OTP verified successfully"));
  }

  @PostMapping("/request-otp")
  public ResponseEntity<SuccessResponse<Void>> requestOtp(@RequestBody @Valid RequestOtpDto payload) {
    
    authService.requestOtp(payload);

    return ResponseEntity.ok(SuccessResponse.message("OTP has been sent to your email"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<SuccessResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordDto payload) {
    
    authService.forgotPassword(payload);

    return ResponseEntity.ok(SuccessResponse.message("OTP has been sent to your email"));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<SuccessResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordDto payload) {
    
    authService.resetPassword(payload);

    return ResponseEntity.ok(SuccessResponse.message("Password reset successfully"));
  }

  @PostMapping("/change-password")
  public ResponseEntity<SuccessResponse<Void>> changePassword(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid ChangePasswordDto payload) {

    authService.changePassword(user, payload);

    return ResponseEntity.ok(SuccessResponse.message("Password changed successfully"));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<SuccessResponse<RefreshTokenResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest payload) {

    RefreshTokenResponse res = authService.refreshToken(payload);

    return ResponseEntity.ok(SuccessResponse.data(res));
  }

  @PostMapping("/logout")
  public ResponseEntity<SuccessResponse<Void>> logout() {
    
    return ResponseEntity.ok(SuccessResponse.message("Logout successful"));
  }
}
