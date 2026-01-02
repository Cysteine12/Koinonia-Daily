package org.eni.koinoniadaily.modules.auth;

import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.eni.koinoniadaily.modules.user.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
  
  public UserPrincipal getCurrentUser() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("User is not authenticated");
    }

    Object principal = authentication.getPrincipal();

    if (!(principal instanceof UserPrincipal)) {
      throw new UnauthorizedException("Invalid authentication principal ");
    }

    return (UserPrincipal) principal;
  }

  public Long getCurrentUserId() {
    
    return getCurrentUser().getId();
  }

  public String getCurrentUserEmail() {

    return getCurrentUser().getEmail();
  }

  public boolean isCurrentUserAdmin() {

    return getCurrentUser().getRole().equals(UserRole.ADMIN);
  }
}
