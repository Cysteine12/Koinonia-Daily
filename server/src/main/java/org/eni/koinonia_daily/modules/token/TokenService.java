package org.eni.koinonia_daily.modules.token;

import java.time.LocalDateTime;

import org.eni.koinonia_daily.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;
  private final TokenUtil tokenUtil;

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
  
  public String generateAndSaveOtp(String email, TokenType type) {

    String otp = tokenUtil.generateOtp();

    create(email, otp, type, LocalDateTime.now().plusMinutes(15));
    
    return otp;
  }

  public void consumeEmailOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.VERIFY_EMAIL, otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    if (token.isExpired()) {
      throw new UnauthorizedException("Expired OTP");
    }
    token.setUsed(true);
  }

  public void consumePasswordOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.CHANGE_PASSWORD, otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    if (token.isExpired()) {
      throw new UnauthorizedException("OTP expired. A new one has been sent.");
    }
    token.setUsed(true);
  }

  public void consumeRefreshToken(String email, String refreshToken) {
    
    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.REFRESH_TOKEN, refreshToken)
                    .orElseThrow(() -> new UnauthorizedException("Invalid token"));

    if (token.isUsed()) {
      tokenRepository.markAllUsedByEmailAndType(email, TokenType.REFRESH_TOKEN);
      
      throw new UnauthorizedException("Revoked token");
    }

    if (token.isExpired()) {
      throw new UnauthorizedException("Expired token");
    }
    token.setUsed(true);
  }
}
