package org.eni.koinonia_daily.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class JwtPayload {

  private final String subject;

  private final String jti;
}
