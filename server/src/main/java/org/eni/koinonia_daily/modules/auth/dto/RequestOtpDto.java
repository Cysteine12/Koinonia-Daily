package org.eni.koinonia_daily.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestOtpDto {
  
  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;
}
