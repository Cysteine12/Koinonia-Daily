package org.eni.koinoniadaily.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.eni.koinoniadaily.modules.auth.JwtService;
import org.eni.koinoniadaily.modules.auth.UserPrincipal;
import org.eni.koinoniadaily.modules.auth.dto.JwtPayload;
import org.eni.koinoniadaily.modules.token.TokenType;
import org.eni.koinoniadaily.utils.ErrorResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;

  private static final String USER_ID_MDC_KEY = "userId";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);
    
    try {
      JwtPayload jwtPayload = jwtService.validateAndExtractPayload(token, TokenType.ACCESS_TOKEN);

      UserDetails userDetails = userDetailsService.loadUserByUsername(jwtPayload.getSubject());

      if (userDetails instanceof UserPrincipal principal) {
        MDC.put(USER_ID_MDC_KEY, String.valueOf(principal.getId()));
      }

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      
      SecurityContextHolder.getContext().setAuthentication(authToken);

    } catch (JwtException | UnauthorizedException | UsernameNotFoundException ex) {
      
      SecurityContextHolder.clearContext();

      ErrorResponse errorResponse = ErrorResponse.builder()
                                      .success(false)
                                      .status(HttpStatus.UNAUTHORIZED.value())
                                      .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                      .message(ex.getMessage())
                                      .path(request.getRequestURI())
                                      .timestamp(LocalDateTime.now())
                                      .build();

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      objectMapper.writeValue(response.getWriter(), errorResponse);
      return;
    }

    filterChain.doFilter(request, response);
  }
  
}
