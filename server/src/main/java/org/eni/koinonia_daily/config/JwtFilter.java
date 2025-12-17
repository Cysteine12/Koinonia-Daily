package org.eni.koinonia_daily.config;

import java.io.IOException;

import org.eni.koinonia_daily.exceptions.UnauthorizedException;
import org.eni.koinonia_daily.modules.auth.JwtService;
import org.eni.koinonia_daily.modules.token.TokenType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
      String subject = jwtService.validateAndExtractSubject(token, TokenType.ACCESS_TOKEN);

      UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      
      SecurityContextHolder.getContext().setAuthentication(authToken);

    } catch (JwtException | UnauthorizedException ex) {
      
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("""
          {
            "success": false,
            "message": \""""
                + ex.getMessage().substring(0, 11) +
                """
          "}
          """);
      return;
    }

    filterChain.doFilter(request, response);
  }
  
}
