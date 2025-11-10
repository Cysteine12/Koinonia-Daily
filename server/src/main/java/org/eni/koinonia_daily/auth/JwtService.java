package org.eni.koinonia_daily.auth;

import java.security.Key;
// import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// @Component
public class JwtService {

  private final Key key;

  public JwtService(@Value("${jwt.secret}") Key secret) {
    this.key = secret;
  }
}
