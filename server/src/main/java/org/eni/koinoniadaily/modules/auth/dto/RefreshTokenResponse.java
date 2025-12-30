package org.eni.koinoniadaily.modules.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {

  private final String accessToken;

  private final String refreshToken;
}
