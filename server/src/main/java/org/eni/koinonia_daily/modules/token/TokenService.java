package org.eni.koinonia_daily.modules.token;

import java.time.LocalDateTime;

import org.eni.koinonia_daily.exceptions.UnauthorizedException;
import org.eni.koinonia_daily.modules.user.User;
import org.eni.koinonia_daily.services.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;
  private final EmailService emailService;

  public void create(String email, String value, TokenType type, LocalDateTime expiresAt) {

    Token token = Token.builder()
                    .email(email)
                    .value(value)
                    .type(type)
                    .expiresAt(expiresAt)
                    .build();
                
    tokenRepository.save(token);
  }
  
  public String generateAndSaveOtp(String email, TokenType type) {

    TokenUtil util = new TokenUtil();
    String otp = util.generateOtp();

    create(email, otp, type, LocalDateTime.now().plusMinutes(15));
    
    return otp;
  }

  @Transactional
  public void consumeEmailOtp(User user, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(
                    user.getEmail(), 
                    TokenType.VERIFY_EMAIL, 
                    otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));
    
    tokenRepository.deleteById(token.getId());

    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      String newOtp = generateAndSaveOtp(user.getEmail(), TokenType.VERIFY_EMAIL);

      emailService.sendEmailVerificationRequestMail(user.getEmail(), user.getFirstName(), newOtp);

      throw new UnauthorizedException("OTP expired. A new one has been sent.");
    }
  }

  @Transactional
  public void consumePasswordOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(
                    email, 
                    TokenType.CHANGE_PASSWORD, 
                    otp)
                    .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

    tokenRepository.deleteById(token.getId());

    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      String newOtp = generateAndSaveOtp(email, TokenType.CHANGE_PASSWORD);

      emailService.sendForgotPasswordMail(email, newOtp);

      throw new UnauthorizedException("OTP expired. A new one has been sent.");
    }
  }

  @Transactional
  public void consumeRefreshToken(String email, String refreshToken) {
    
    Token token = tokenRepository.findByEmailAndTypeAndValue(email, TokenType.REFRESH_TOKEN, refreshToken)
                    .orElseThrow(() -> {
                      tokenRepository.deleteAllByEmail(email);

                      return new UnauthorizedException("Revoked token");
                    });
        
    tokenRepository.deleteById(token.getId());

    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      
      throw new UnauthorizedException("Expired token");
    }
  }
}
