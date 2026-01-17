package org.eni.koinoniadaily.config.ratelimit;

import java.io.IOException;
import java.time.LocalDateTime;

import org.eni.koinoniadaily.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
  
  private final ObjectMapper objectMapper;
  private final RateLimitBucketService bucketService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    String clientIP = request.getRemoteAddr();

    String ipBucketKey = "ip:" + clientIP;

    if(!bucketService.consume(ipBucketKey, RateLimitPolicy::ipBucket)) {
      sendRateLimitExceededResponse(request, response);
      return;
    }

    String sensitiveEndpointBucketKey = "sensitive:" + clientIP + ":" + request.getRequestURI();

    if(isSensitiveEndpoint(request) && !bucketService.consume(sensitiveEndpointBucketKey, RateLimitPolicy::sensitiveEndpointBucket)) {
      sendRateLimitExceededResponse(request, response);
      return;
    }
    
    filterChain.doFilter(request, response);
  }

  private boolean isSensitiveEndpoint(HttpServletRequest request) {

    String uri = request.getRequestURI();

    return uri.startsWith("/api/auth/login") ||
           uri.startsWith("/api/auth/register") ||
           uri.startsWith("/api/auth/verify-email") ||
           uri.startsWith("/api/auth/request-otp") ||
           uri.startsWith("/api/auth/forgot-password") ||
           uri.startsWith("/api/auth/reset-password") ||
           uri.startsWith("/api/auth/refresh-token");
  }

  private void sendRateLimitExceededResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

    ErrorResponse error = ErrorResponse.builder()
                            .success(false)
                            .status(HttpStatus.TOO_MANY_REQUESTS.value())
                            .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                            .message("Too many request. Please try again later.")
                            .path(request.getRequestURI())
                            .timestamp(LocalDateTime.now())
                            .build();     
    
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), error);
  }
}
