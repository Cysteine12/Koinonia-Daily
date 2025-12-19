package org.eni.koinonia_daily.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPayload {

  private final String subject;

  private final String jti;
}
