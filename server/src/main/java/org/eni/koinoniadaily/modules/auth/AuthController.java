package org.eni.koinoniadaily.modules.auth;

import org.eni.koinoniadaily.modules.auth.dto.ChangePasswordDto;
import org.eni.koinoniadaily.modules.auth.dto.ForgotPasswordDto;
import org.eni.koinoniadaily.modules.auth.dto.LoginRequest;
import org.eni.koinoniadaily.modules.auth.dto.LoginResponse;
import org.eni.koinoniadaily.modules.auth.dto.RefreshTokenRequest;
import org.eni.koinoniadaily.modules.auth.dto.RefreshTokenResponse;
import org.eni.koinoniadaily.modules.auth.dto.RegisterRequest;
import org.eni.koinoniadaily.modules.auth.dto.RequestOtpDto;
import org.eni.koinoniadaily.modules.auth.dto.ResetPasswordDto;
import org.eni.koinoniadaily.modules.auth.dto.VerifyEmailDto;
import org.eni.koinoniadaily.utils.SuccessResponse;
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
  public ResponseEntity<SuccessResponse<Void>> register(@RequestBody @Valid RegisterRequest request) {

    authService.register(request);

    return ResponseEntity.ok(SuccessResponse.message("Registration successful. OTP has been sent to your email for verification."));
  }

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
    
    LoginResponse response = authService.login(request);

    return ResponseEntity.ok(SuccessResponse.of(response, "Login Successful"));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<SuccessResponse<Void>> verifyEmail(@RequestBody @Valid VerifyEmailDto request) {
    
    authService.verifyEmail(request);

    return ResponseEntity.ok(SuccessResponse.message("OTP verified successfully"));
  }

  @PostMapping("/request-otp")
  public ResponseEntity<SuccessResponse<Void>> requestOtp(@RequestBody @Valid RequestOtpDto request) {
    
    authService.requestOtp(request);

    return ResponseEntity.ok(SuccessResponse.message("OTP has been sent to your email"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<SuccessResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
    
    authService.forgotPassword(request);

    return ResponseEntity.ok(SuccessResponse.message("OTP has been sent to your email"));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<SuccessResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordDto request) {
    
    authService.resetPassword(request);

    return ResponseEntity.ok(SuccessResponse.message("Password reset successfully"));
  }

  @PostMapping("/change-password")
  public ResponseEntity<SuccessResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordDto request) {

    authService.changePassword(request);

    return ResponseEntity.ok(SuccessResponse.message("Password changed successfully"));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<SuccessResponse<RefreshTokenResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {

    RefreshTokenResponse response = authService.refreshToken(request);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping("/logout")
  public ResponseEntity<SuccessResponse<Void>> logout(
      @RequestBody @Valid RefreshTokenRequest request
  ) {
    authService.logout(request);
    
    return ResponseEntity.ok(SuccessResponse.message("Logout successful"));
  }
}
