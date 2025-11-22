package org.eni.koinonia_daily.modules.auth.dto;

import java.time.LocalDateTime;

import org.eni.koinonia_daily.modules.user.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter @AllArgsConstructor
public class UserProfileDto {

  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private UserRole role;

  private LocalDateTime createdAt;
}
