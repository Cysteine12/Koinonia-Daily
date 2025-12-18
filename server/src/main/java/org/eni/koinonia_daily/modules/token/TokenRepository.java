package org.eni.koinonia_daily.modules.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByEmailAndType(String email, TokenType type);

  Optional<Token> findByEmailAndTypeAndValue(String email, TokenType type, String value);

  void deleteAllByEmail(String email);
}
