package org.eni.koinoniadaily.modules.account.dto;

import java.time.LocalDateTime;

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

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
