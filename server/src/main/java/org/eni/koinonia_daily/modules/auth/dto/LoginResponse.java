package org.eni.koinonia_daily.modules.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

  private final String accessToken;
  
  private final String refreshToken;
}
