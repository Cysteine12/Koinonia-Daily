package org.eni.koinoniadaily.modules.token;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.eni.koinoniadaily.modules.auth.JwtService;
import org.eni.koinoniadaily.modules.auth.dto.TokenPair;
import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private static final int OTP_EXPIRATION_MINUTES = 15;
  private static final long ACCESS_TOKEN_EXPIRATION_MS = Duration.ofDays(1).toMillis();
  private static final long REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(14).toMillis();

  private void create(String email, String value, TokenType type, LocalDateTime expiresAt) {

    Token token = Token.builder()
                    .email(email)
                    .value(value)
                    .type(type)
                    .isUsed(false)
                    .expiresAt(expiresAt)
                    .build();
                
    tokenRepository.save(token);
  }
  
  @Transactional
  public String generateAndSaveOtp(String email, TokenType type) {

    String otp = TokenUtil.generateOtp();

    create(email, otp, type, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
    
    return otp;
  }

  @Transactional
  public void consumeEmailOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.VERIFY_EMAIL, otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    if (token.isUsed()) {
      revokeAllTokens(email, TokenType.VERIFY_EMAIL);

      throw new UnauthorizedException("Revoked token");
    }
                    
    if (token.isExpired()) {
      throw new UnauthorizedException("Expired OTP");
    }
    token.setUsed(true);
  }

  @Transactional
  public void consumePasswordOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.CHANGE_PASSWORD, otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    if (token.isUsed()) {
      revokeAllTokens(email, TokenType.CHANGE_PASSWORD);
      
      throw new UnauthorizedException("Revoked token");
    }
      
    if (token.isExpired()) {
      throw new UnauthorizedException("Expired OTP");
    }
    token.setUsed(true);
  }

  @Transactional
  public TokenPair generateAndSaveTokens(String email) {
    
    String jti = UUID.randomUUID().toString();

    String accessToken = jwtService.generateToken(email, ACCESS_TOKEN_EXPIRATION_MS, TokenType.ACCESS_TOKEN, null);
    String refreshToken = jwtService.generateToken(email, REFRESH_TOKEN_EXPIRATION_MS, TokenType.REFRESH_TOKEN, jti);

    create(email, jti, TokenType.REFRESH_TOKEN, LocalDateTime.now().plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_MS)));

    return new TokenPair(accessToken, refreshToken);
  }

  @Transactional
  public void consumeRefreshToken(String email, String refreshToken) {
    
    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.REFRESH_TOKEN, refreshToken)
                    .orElseThrow(() -> new UnauthorizedException("Invalid token"));

    if (token.isUsed()) {
      revokeAllTokens(email, TokenType.REFRESH_TOKEN);
      
      throw new UnauthorizedException("Revoked token");
    }

    if (token.isExpired()) {
      throw new UnauthorizedException("Expired token");
    }
    token.setUsed(true);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void revokeAllTokens(String email, TokenType type) {
    tokenRepository.markAllUsedByEmailAndType(email, type);
  }
}
