package org.eni.koinonia_daily.auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final Key key;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(String email, int expiryInMs) {
    
    Map<String, Object> claims = new HashMap<>();
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

  public String getEmailFromToken(String token) {
    return parseClaims(token).getSubject();
  }

  public boolean validateToken(String token, String email) {

    Claims claims = parseClaims(token);

    return claims.getSubject().equals(email) && 
            claims.getExpiration().after(new Date());
  }
}
