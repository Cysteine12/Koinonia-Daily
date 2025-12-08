package org.eni.koinonia_daily.modules.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.eni.koinonia_daily.exceptions.ValidationException;
import org.eni.koinonia_daily.modules.user.User;
import org.eni.koinonia_daily.services.EmailService;
import org.springframework.stereotype.Service;

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

    Optional<Token> savedToken = tokenRepository.findByEmailAndType(email, type);
    if (savedToken.isPresent()) {
      token.setId(savedToken.get().getId());
    }
                
    tokenRepository.save(token);
  }
  
  public String generateAndSaveOtp(String email, TokenType type) {

    TokenUtil util = new TokenUtil();
    String otp = util.generateOtp();

    create(email, otp, type, LocalDateTime.now().plusMinutes(15));
    
    return otp;
  }

  public void verifyEmailOtp(User user, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(
                    user.getEmail(), 
                    TokenType.VERIFY_EMAIL, 
                    otp)
                    .orElseThrow(() -> new ValidationException("Invalid OTP"));

    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      String newOtp = generateAndSaveOtp(user.getEmail(), TokenType.VERIFY_EMAIL);

      emailService.sendEmailVerificationRequestMail(user.getEmail(), user.getFirstName(), newOtp);

      throw new ValidationException("OTP expired. A new one has been sent.");
    }
    tokenRepository.deleteById(token.getId());
  }

  public void verifyPasswordOtp(String email, String otp) {

    Token token = tokenRepository.findByEmailAndTypeAndValue(
                    email, 
                    TokenType.CHANGE_PASSWORD, 
                    otp)
                    .orElseThrow(() -> new ValidationException("Invalid OTP"));

    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      String newOtp = generateAndSaveOtp(email, TokenType.VERIFY_EMAIL);

      emailService.sendForgotPasswordMail(email, newOtp);

      throw new ValidationException("OTP expired. A new one has been sent.");
    }
    tokenRepository.deleteById(token.getId());
  }
}
