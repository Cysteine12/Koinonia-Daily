package org.eni.koinoniadaily.config.ratelimit;

import java.io.IOException;
import java.time.Instant;
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
    "/api/v1/auth/login",
    "/api/v1/auth/register",
    "/api/v1/auth/verify-email",
    "/api/v1/auth/request-otp",
    "/api/v1/auth/forgot-password",
    "/api/v1/auth/reset-password",
    "/api/v1/auth/refresh-token"
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

  /**
   * Sends a 429 (Too Many Requests) JSON error response for the provided request.
   *
   * The response payload contains a standardized ErrorResponse with fields:
   * success=false, status=429, error reason, message, request path, code="RATE_LIMIT_EXCEEDED",
   * and a timestamp. The HTTP status and Content-Type are set on the response before writing.
   *
   * @param request  the incoming HTTP request whose URI is included in the error payload
   * @param response the HTTP response used to set status, content type, and write the JSON body
   * @throws IOException if writing the JSON response fails
   */
  private void sendRateLimitExceededResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

    ErrorResponse error = ErrorResponse.builder()
                            .success(false)
                            .status(HttpStatus.TOO_MANY_REQUESTS.value())
                            .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                            .message("Too many requests. Please try again later.")
                            .path(request.getRequestURI())
                            .code("RATE_LIMIT_EXCEEDED")
                            .timestamp(Instant.now())
                            .build();     
    
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), error);
  }
}
