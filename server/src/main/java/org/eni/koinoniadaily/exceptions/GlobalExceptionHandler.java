package org.eni.koinoniadaily.exceptions;

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

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Builds a ResponseEntity containing an ErrorResponse populated with the given message, HTTP status, and code, with no additional errors.
   *
   * @param request the current web request (used to extract request path)
   * @param message the human-readable error message to include in the response
   * @param status the HTTP status to return
   * @param code an application-specific error code to include in the response
   * @return a ResponseEntity wrapping an ErrorResponse with the provided fields and no errors payload
   */
  private ResponseEntity<ErrorResponse> buildResponse(WebRequest request, String message, HttpStatus status, String code) {
    return buildResponse(request, message, status, code, null);
  }

  /**
   * Builds a ResponseEntity containing an ErrorResponse populated from the request and provided details.
   *
   * @param request the current web request (used to extract the request URI)
   * @param message a human-readable message describing the error
   * @param status the HTTP status to apply to both the response and the ErrorResponse
   * @param code an application-specific error code to include in the ErrorResponse
   * @param errors optional additional error details to include in the ErrorResponse; may be null
   * @return a ResponseEntity wrapping the constructed ErrorResponse with the given HTTP status
   */
  private ResponseEntity<ErrorResponse> buildResponse(WebRequest request, String message, HttpStatus status, String code, Object errors) {
    ErrorResponse error = ErrorResponse.builder()
                            .success(false)
                            .status(status.value())
                            .error(status.getReasonPhrase())
                            .message(message)
                            .path(((ServletWebRequest) request).getRequest().getRequestURI())
                            .code(code)
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
    
  /**
   * Builds a 401 UNAUTHORIZED error response using the exception's message and code.
   *
   * @return a ResponseEntity containing an ErrorResponse with HTTP status 401, the exception message, and the exception's code
   */
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {

    return buildResponse(request, ex.getMessage(), HttpStatus.UNAUTHORIZED, ex.getCode());
  }
    
  // Handle lock exceptions (for token) 
  @ExceptionHandler(OptimisticLockException.class)
  public ResponseEntity<ErrorResponse> handleOptimisticLockException(OptimisticLockException ex, WebRequest request) {

    return buildResponse(request, "Resource is currently locked. Please try again later.", HttpStatus.CONFLICT, "RESOURCE_LOCKED");
  }

  // Handle email sending exceptions
  @ExceptionHandler(EmailSendingException.class)
  public ResponseEntity<ErrorResponse> handleEmailSendingException(EmailSendingException ex, WebRequest request) {

    return buildResponse(request, "Unable to send email at the moment. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE, "EMAIL_FAILED");
  }

  // Handle all other exceptions (catch-all)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {

    logger.error("Unhandled exception", ex);
        
    return buildResponse(request, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
  }
}
