package org.eni.koinonia_daily.modules.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

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
}
