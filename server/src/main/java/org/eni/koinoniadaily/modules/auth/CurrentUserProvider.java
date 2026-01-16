package org.eni.koinoniadaily.modules.auth;

import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.eni.koinoniadaily.modules.user.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class CurrentUserProvider {
  
  private UserPrincipal cachedUserPrincipal;
  
  public UserPrincipal getCurrentUser() {

    if (cachedUserPrincipal == null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  
      if (authentication == null || !authentication.isAuthenticated()) {
        throw new UnauthorizedException("User is not authenticated");
      }
  
      Object principal = authentication.getPrincipal();
  
      if (!(principal instanceof UserPrincipal)) {
        throw new UnauthorizedException("Invalid authentication principal");
      }
      cachedUserPrincipal = (UserPrincipal) principal;
    }

    return cachedUserPrincipal;
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
