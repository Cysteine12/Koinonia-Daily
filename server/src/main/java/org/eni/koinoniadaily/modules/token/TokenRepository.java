package org.eni.koinoniadaily.modules.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByEmailAndType(String email, TokenType type);

  Optional<Token> findByEmailAndTypeAndValue(String email, TokenType type, String value);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("UPDATE Token t SET t.isUsed = true WHERE t.email = :email AND t.type = :type")
  int markAllUsedByEmailAndType(@Param("email") String email, @Param("type") TokenType type);
}
