package org.eni.koinoniadaily.modules.auth;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

  private static final long serialVersionUID = 1L;
  
  private final Long id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final UserRole role;
  private final boolean isVerified;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  private final String password;
  private final String username;

  public UserPrincipal(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.isVerified = user.isVerified();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
    this.password = user.getPassword();
    this.username = user.getEmail();
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
  }
}
