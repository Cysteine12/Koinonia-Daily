package org.eni.koinonia_daily.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangePasswordDto {
  
  @NotBlank(message = "Current password is required")
  @Size(min = 7, message = "Minimum password length of 7 required")
  private String currentPassword;

  @NotBlank(message = "New password is required")
  @Size(min = 7, message = "Minimum password length of 7 required")
  private String newPassword;
}
