package org.eni.koinoniadaily.modules.token;

import java.time.LocalDateTime;

import org.eni.koinoniadaily.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor 
public class Token extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String email;

  @Column(nullable = false, length = 255)
  private String value;

  @Column(name = "'type'", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private TokenType type;
  
  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  @Builder.Default
  private boolean isUsed = false;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @Version
  private Long version;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
