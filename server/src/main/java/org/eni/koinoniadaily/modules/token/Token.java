package org.eni.koinoniadaily.modules.token;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tokens")
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class Token {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false, length = 50)
  private String email;

  @Column(nullable = false, length = 255)
  private String value;

  @Column(name = "'type'", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private TokenType type;
  
  @Builder.Default
  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean isUsed = false;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
