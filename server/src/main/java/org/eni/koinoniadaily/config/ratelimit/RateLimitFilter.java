package org.eni.koinoniadaily.config.ratelimit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

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
  private static final Set<String> SENSITIVE_ENDPOINTS = Set.of(
    "/api/auth/login",
    "/api/auth/register",
    "/api/auth/verify-email",
    "/api/auth/request-otp",
    "/api/auth/forgot-password",
    "/api/auth/reset-password",
    "/api/auth/refresh-token"
  );

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

    return SENSITIVE_ENDPOINTS.stream().anyMatch(uri::startsWith);
  }

  private void sendRateLimitExceededResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

    ErrorResponse error = ErrorResponse.builder()
                            .success(false)
                            .status(HttpStatus.TOO_MANY_REQUESTS.value())
                            .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                            .message("Too many requests. Please try again later.")
                            .path(request.getRequestURI())
                            .timestamp(LocalDateTime.now())
                            .build();     
    
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), error);
  }
}
