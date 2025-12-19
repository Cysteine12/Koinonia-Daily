package org.eni.koinonia_daily.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RefreshTokenRequest {
  
  @NotBlank(message = "Refresh token is required")
  private String refreshToken;
}
