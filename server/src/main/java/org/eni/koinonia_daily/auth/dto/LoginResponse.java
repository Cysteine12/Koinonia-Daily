package org.eni.koinonia_daily.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter @AllArgsConstructor
public class LoginResponse {
  private String token;

  private String message;
}
