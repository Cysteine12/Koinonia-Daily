package org.eni.koinonia_daily.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPair {

  private final String accessToken;

  private final String refreshToken;
}
