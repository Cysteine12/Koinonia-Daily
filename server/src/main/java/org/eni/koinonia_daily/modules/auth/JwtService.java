package org.eni.koinonia_daily.modules.auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.eni.koinonia_daily.exceptions.UnauthorizedException;
import org.eni.koinonia_daily.modules.token.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final Key key;
  private static final String TOKEN_TYPE_KEY = "token_type";

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(String email, int expiryInMs, TokenType type) {
    
    Map<String, Object> claims = new HashMap<>();
    claims.put(TOKEN_TYPE_KEY, type.name());
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiryInMs);

    return Jwts.builder()
            .claims()
            .add(claims)
            .subject(email)
            .issuedAt(now)
            .expiration(expiry)
            .and()
            .signWith(key)
            .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parser()
            .verifyWith((SecretKey)key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public String validateAndExtractSubject(String token, TokenType type) {
    Claims claims = parseClaims(token);

    if (claims.getExpiration().before(new Date())) {
      throw new UnauthorizedException("Expired token");
    }

    String tokenType = claims.get(TOKEN_TYPE_KEY, String.class);

    if (!tokenType.equals(type.name())) {
      throw new UnauthorizedException("Invalid token");
    }

    return claims.getSubject();
  }
}
