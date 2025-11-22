package org.eni.koinonia_daily.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter @AllArgsConstructor
public class LoginResponse {

  private String accessToken;
  
  private String refreshToken;

  private String message;
}
