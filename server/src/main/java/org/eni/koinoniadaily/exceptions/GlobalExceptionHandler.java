package org.eni.koinoniadaily.exceptions;

import org.eni.koinoniadaily.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.OptimisticLockException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ErrorResponse> buildResponse(WebRequest request, String message, HttpStatus status) {
    ErrorResponse error = ErrorResponse.builder()
                            .success(false)
                            .status(status.value())
                            .error(status.getReasonPhrase())
                            .message(message)
                            .path(((ServletWebRequest) request).getRequest().getRequestURI())
                            .timestamp(LocalDateTime.now())
                            .build();
                        
    return new ResponseEntity<>(error, status);
  }

  // Handle resource not found (e.g., when user not found)
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
    
    return buildResponse(request, ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  // Handle validation errors (for @Valid annotated DTOs)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

    String errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.joining(", "));

    return buildResponse(request, errors, HttpStatus.BAD_REQUEST);
  }

  // Handle runtime validation errors
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
     
    return buildResponse(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Handle bad credentials error (for login,..)
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {

    return buildResponse(request, "Invalid credentials", HttpStatus.UNAUTHORIZED);
  }
    
  // Handle unauthorized errors
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {

    return buildResponse(request, ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }
    
  // Handle lock exceptions (for token) 
  @ExceptionHandler(OptimisticLockException.class)
  public ResponseEntity<ErrorResponse> handleOptimisticLockException(OptimisticLockException ex, WebRequest request) {

    UnauthorizedException exception = new UnauthorizedException("Unable to process request due to a conflict. Please try again.");
    return handleUnauthorizedException(exception, request);
  }

  // Handle all other exceptions (catch-all)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        
    return buildResponse(request, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
