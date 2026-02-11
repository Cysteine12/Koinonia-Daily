package org.eni.koinoniadaily.modules.account.dto;

import java.time.Instant;

import org.eni.koinoniadaily.modules.user.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountProfileResponse {
  
  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private String photoUrl;

  private UserRole role;

  private Instant createdAt;

  private Instant updatedAt;
}
