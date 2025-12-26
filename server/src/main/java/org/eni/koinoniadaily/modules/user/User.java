package org.eni.koinoniadaily.modules.user;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(nullable = false, length = 50)
  private String lastName;

  @Column(nullable = false, length = 50, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Builder.Default
  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean isVerified = false;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    if (this.role == null) {
      this.role = UserRole.USER;
    }

  }
}
