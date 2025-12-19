package org.eni.koinonia_daily.modules.token;

import java.time.LocalDateTime;

import org.eni.koinonia_daily.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;
  private final TokenUtil tokenUtil;
  private static final int OTP_EXPIRATION_MINUTES = 15;

  public void create(String email, String value, TokenType type, LocalDateTime expiresAt) {

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

    tokenRepository.markAllUsedByEmailAndType(email, type);

    String otp = tokenUtil.generateOtp();

    create(email, otp, type, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
    
    return otp;
  }

  @Transactional
  public void consumeEmailOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.VERIFY_EMAIL, otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    if (token.isExpired()) {
      throw new UnauthorizedException("Expired OTP");
    }
    token.setUsed(true);
  }

  @Transactional
  public void consumePasswordOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.CHANGE_PASSWORD, otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    if (token.isExpired()) {
      throw new UnauthorizedException("Expired OTP");
    }
    token.setUsed(true);
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
  private void revokeAllTokens(String email, TokenType type) {
    tokenRepository.markAllUsedByEmailAndType(email, type);
  }
}
