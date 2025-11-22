package org.eni.koinonia_daily.modules.auth;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.eni.koinonia_daily.modules.user.User;
import org.eni.koinonia_daily.modules.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
  private final Long id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final UserRole role;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  private final User user;

  public UserPrincipal(User user) {
    this.user = user;
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
  }
}
