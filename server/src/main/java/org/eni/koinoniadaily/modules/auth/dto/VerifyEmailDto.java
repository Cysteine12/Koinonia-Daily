package org.eni.koinoniadaily.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class VerifyEmailDto {
  
  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "OTP is required")
  @Size(max = 6, min = 6, message = "6 digit OTP required")
  private String otp;
}
