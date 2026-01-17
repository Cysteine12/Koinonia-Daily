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

  /**
   * Enforces per-client IP and per-sensitive-endpoint rate limits, continuing the filter chain when allowed or sending a 429 response when limits are exceeded.
   *
   * @param request     the incoming HTTP request
   * @param response    the HTTP response used to send a 429 error when rate limits are exceeded
   * @param filterChain the filter chain to continue processing when the request is allowed
   * @throws ServletException if an error occurs during downstream filter processing
   * @throws IOException      if an I/O error occurs while writing the error response or during downstream processing
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    String clientIP = getClientIP(request);

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

  /**
   * Determine the client's IP address, preferring the first entry in the `X-Forwarded-For` header when present.
   *
   * @param request the HTTP servlet request to extract the client IP from
   * @return the client's IP address: the first IP from the `X-Forwarded-For` header if present, otherwise the request's remote address
   */
  private String getClientIP(HttpServletRequest request) {

    String header = request.getHeader("X-Forwarded-For");
    if (header == null) {
      return request.getRemoteAddr();
    }
    return header.split(",")[0].trim();
  }

  /**
   * Determines whether the incoming request targets an authentication-related sensitive endpoint.
   *
   * @return `true` if the request URI starts with one of the authentication-sensitive paths
   *         ("/api/auth/login", "/api/auth/register", "/api/auth/verify-email",
   *         "/api/auth/request-otp", "/api/auth/forgot-password", "/api/auth/reset-password",
   *         "/api/auth/refresh-token"), `false` otherwise.
   */
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

  /**
   * Sends a 429 Too Many Requests JSON response describing that the client has exceeded rate limits.
   *
   * Constructs an ErrorResponse (including path and timestamp) and writes it as JSON to the response
   * with HTTP status 429 and content type application/json.
   *
   * @param request  used to populate the error path
   * @param response the HTTP response to which the JSON error will be written
   * @throws IOException if writing the JSON to the response fails
   */
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