package org.eni.koinoniadaily.modules.user;

import org.eni.koinoniadaily.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor 
@AllArgsConstructor
public class User extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(nullable = false, length = 50)
  private String lastName;

  @Column(nullable = false, length = 50, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true, length = 100)
  private String photoUrl;

  @Column(nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Builder.Default
  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean isVerified = false;

  @PrePersist
  public void prePersist() {
    if (this.role == null) {
      this.role = UserRole.USER;
    }

  }
}
