package org.eni.koinonia_daily.auth;

import org.eni.koinonia_daily.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
            .findByEmail(username)
            .map(UserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("Invalid credientials"));
  }  
}
