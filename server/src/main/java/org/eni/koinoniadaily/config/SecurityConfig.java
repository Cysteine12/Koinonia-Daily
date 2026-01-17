package org.eni.koinoniadaily.config;

import org.eni.koinoniadaily.config.ratelimit.RateLimitFilter;
import org.eni.koinoniadaily.modules.auth.JpaUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JpaUserDetailsService userDetailsService;
  private final RateLimitFilter rateLimitFilter;
  private final JwtFilter jwtFilter;
  
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(customizer -> customizer.disable())
            .authorizeHttpRequests(request -> request
                                    .requestMatchers("/api/auth/profile", "/api/auth/change-password").authenticated()
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtFilter, RateLimitFilter.class)
            .userDetailsService(userDetailsService)
            .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
