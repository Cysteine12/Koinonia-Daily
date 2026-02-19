package org.eni.koinoniadaily.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.utils.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.OptimisticLockException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper;
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  private ResponseEntity<ErrorResponse> buildResponse(WebRequest request, String message, HttpStatus status, String errorCode) {
    return buildResponse(request, message, status, errorCode, null);
  }

  private ResponseEntity<ErrorResponse> buildResponse(WebRequest request, String message, HttpStatus status, String errorCode, Object errors) {
    ErrorResponse error = ErrorResponse.builder()
                            .success(false)
                            .status(status.value())
                            .error(status.getReasonPhrase())
                            .message(message)
                            .path(((ServletWebRequest) request).getRequest().getRequestURI())
                            .errorCode(errorCode)
                            .errors(errors)
                            .timestamp(Instant.now())
                            .build();
                        
    return new ResponseEntity<>(error, status);
  }

  // Handle resource not found (e.g., when user not found)
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
    
    return buildResponse(request, ex.getMessage(), HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
  }

  // Handle validation errors (for @Valid annotated DTOs)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {

    Map<String, String> errors = ex.getBindingResult()
                                      .getFieldErrors()
                                      .stream()
                                      .collect(Collectors.toMap(
                                          FieldError::getField,
                                          error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                                          (existing, replacement) -> existing + ";" + replacement
                                      ));

    return buildResponse(request, "Validation failed", HttpStatus.UNPROCESSABLE_ENTITY, "VALIDATION_ERROR", errors);
  }

  // Handle runtime validation errors
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
     
    return buildResponse(request, ex.getMessage(), HttpStatus.BAD_REQUEST, ex.getCode());
  }

  // Handle bad credentials error (for login,..)
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {

    return buildResponse(request, "Invalid credentials", HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS");
  }
    
  // Handle unauthorized errors
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {

    return buildResponse(request, ex.getMessage(), HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
  }
    
  /**
   * Handles optimistic lock conflicts raised when a resource is concurrently modified.
   *
   * @return ResponseEntity containing an ErrorResponse with HTTP status 409 CONFLICT,
   *         message "Resource is currently locked. Please try again later.", and error code "RESOURCE_LOCKED".
   */
  @ExceptionHandler(OptimisticLockException.class)
  public ResponseEntity<ErrorResponse> handleOptimisticLockException(OptimisticLockException ex, WebRequest request) {

    return buildResponse(request, "Resource is currently locked. Please try again later.", HttpStatus.CONFLICT, "RESOURCE_LOCKED");
  }

  /**
   * Handle EmailSendingException by returning a Service Unavailable error response.
   *
   * @param ex the EmailSendingException that was thrown
   * @param request the current WebRequest used to determine request details
   * @return a ResponseEntity containing an ErrorResponse with HTTP 503 (Service Unavailable) and error code "EMAIL_FAILED"
   */
  @ExceptionHandler(EmailSendingException.class)
  public ResponseEntity<ErrorResponse> handleEmailSendingException(EmailSendingException ex, WebRequest request) {

    return buildResponse(request, "Unable to send email at the moment. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE, "EMAIL_FAILED");
  }

  /**
   * Handles all uncaught exceptions and produces a standardized 500 Internal Server Error response.
   *
   * @param ex the uncaught exception
   * @param request the current web request
   * @return a ResponseEntity containing an ErrorResponse with HTTP status 500 and error code "INTERNAL_SERVER_ERROR"
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {

    logger.error("Unhandled exception", ex);
        
    return buildResponse(request, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
  }
}