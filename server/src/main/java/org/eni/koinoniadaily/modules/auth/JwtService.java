package org.eni.koinoniadaily.modules.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.eni.koinoniadaily.modules.auth.dto.JwtPayload;
import org.eni.koinoniadaily.modules.token.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public final class JwtService {

  private final Key key;
  private static final String TOKEN_TYPE_KEY = "token_type";

  public JwtService(@Value("${jwt.secret}") String secret) {

    if (secret == null || secret.trim().isEmpty()) {
      throw new IllegalArgumentException("JWT secret cannot be null or empty");
    }
    
    try {
      this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      
      throw new IllegalStateException("Failed to initialize JWT key", e);
    }
  }

  public String generateToken(String subject, long expiryInMs, TokenType type, String jti) {
    
    Map<String, Object> claims = new HashMap<>();
    claims.put(TOKEN_TYPE_KEY, type.name());

    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiryInMs);

    return Jwts.builder()
            .claims()
            .add(claims)
            .id(jti)
            .subject(subject)
            .issuedAt(now)
            .expiration(expiry)
            .and()
            .signWith(key)
            .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parser()
            .verifyWith((SecretKey) key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public JwtPayload validateAndExtractPayload(String token, TokenType type) {
    Claims claims = parseClaims(token);

    if (claims.getExpiration().before(new Date())) {
      throw new UnauthorizedException("Expired token");
    }

    String tokenType = claims.get(TOKEN_TYPE_KEY, String.class);

    if (!type.name().equals(tokenType)) {
      throw new UnauthorizedException("Invalid token");
    }

    return new JwtPayload(claims.getSubject(), claims.getId());
  }
}
