package org.eni.koinoniadaily.modules.account;

import org.eni.koinoniadaily.modules.account.dto.AccountProfileResponse;
import org.eni.koinoniadaily.modules.auth.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
  
  public AccountProfileResponse toDto(UserPrincipal principal) {

    return AccountProfileResponse.builder()
            .id(principal.getId())
            .firstName(principal.getFirstName())
            .lastName(principal.getLastName())
            .email(principal.getEmail())
            .photoUrl(principal.getPhotoUrl())
            .role(principal.getRole())
            .createdAt(principal.getCreatedAt())
            .updatedAt(principal.getUpdatedAt())
            .build();
  }
}
