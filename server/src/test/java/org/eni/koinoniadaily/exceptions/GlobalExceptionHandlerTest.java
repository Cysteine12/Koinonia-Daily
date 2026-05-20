package org.eni.koinoniadaily.exceptions;

import org.eni.koinoniadaily.utils.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.OptimisticLockException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;
  private WebRequest webRequest;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();

    MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    servletRequest.setRequestURI("/api/v1/test");
    webRequest = new ServletWebRequest(servletRequest);
  }

  // =========================================================================
  // NotFoundException handler
  // =========================================================================

  @Nested
  class NotFoundExceptionTests {

    @Test
    void handleNotFoundException_returns404() {
      NotFoundException ex = new NotFoundException("Resource not found");

      ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleNotFoundException_responseBodyHasCorrectMessage() {
      NotFoundException ex = new NotFoundException("Teaching not found");

      ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, webRequest);

      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getMessage()).isEqualTo("Teaching not found");
    }

    @Test
    void handleNotFoundException_responseBodyHasResourceNotFoundCode() {
      NotFoundException ex = new NotFoundException("Not found");

      ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("RESOURCE_NOT_FOUND");
    }

    @Test
    void handleNotFoundException_responseBodyHasCorrectPath() {
      NotFoundException ex = new NotFoundException("Not found");

      ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, webRequest);

      assertThat(response.getBody().getPath()).isEqualTo("/api/v1/test");
    }

    @Test
    void handleNotFoundException_successIsFalse() {
      NotFoundException ex = new NotFoundException("Not found");

      ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, webRequest);

      assertThat(response.getBody().isSuccess()).isFalse();
    }
  }

  // =========================================================================
  // ValidationException handler
  // =========================================================================

  @Nested
  class ValidationExceptionTests {

    @Test
    void handleValidationException_returns400() {
      ValidationException ex = new ValidationException("Invalid input");

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleValidationException_responseHasExceptionMessage() {
      ValidationException ex = new ValidationException("Teaching has not been chunked");

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      assertThat(response.getBody().getMessage()).isEqualTo("Teaching has not been chunked");
    }

    @Test
    void handleValidationException_usesDefaultCode() {
      ValidationException ex = new ValidationException("Error message");

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    void handleValidationException_withCustomCode_usesCustomCode() {
      ValidationException ex = new ValidationException("EMBEDDING_REJECTED", "Queue saturated");

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("EMBEDDING_REJECTED");
      assertThat(response.getBody().getMessage()).isEqualTo("Queue saturated");
    }
  }

  // =========================================================================
  // BadCredentialsException handler
  // =========================================================================

  @Nested
  class BadCredentialsExceptionTests {

    @Test
    void handleBadCredentialsException_returns401() {
      BadCredentialsException ex = new BadCredentialsException("Bad credentials");

      ResponseEntity<ErrorResponse> response =
          handler.handleBadCredentialsException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleBadCredentialsException_responseHasInvalidCredentialsMessage() {
      BadCredentialsException ex = new BadCredentialsException("Anything");

      ResponseEntity<ErrorResponse> response =
          handler.handleBadCredentialsException(ex, webRequest);

      assertThat(response.getBody().getMessage()).isEqualTo("Invalid credentials");
    }

    @Test
    void handleBadCredentialsException_hasCorrectCode() {
      BadCredentialsException ex = new BadCredentialsException("Bad creds");

      ResponseEntity<ErrorResponse> response =
          handler.handleBadCredentialsException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("BAD_CREDENTIALS");
    }
  }

  // =========================================================================
  // UnauthorizedException handler
  // =========================================================================

  @Nested
  class UnauthorizedExceptionTests {

    @Test
    void handleUnauthorizedException_returns401() {
      UnauthorizedException ex = new UnauthorizedException("Not authorized");

      ResponseEntity<ErrorResponse> response =
          handler.handleUnauthorizedException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
  }

  // =========================================================================
  // OptimisticLockException handler
  // =========================================================================

  @Nested
  class OptimisticLockExceptionTests {

    @Test
    void handleOptimisticLockException_returns409() {
      OptimisticLockException ex = new OptimisticLockException("Locked");

      ResponseEntity<ErrorResponse> response =
          handler.handleOptimisticLockException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleOptimisticLockException_hasResourceLockedCode() {
      OptimisticLockException ex = new OptimisticLockException("Locked");

      ResponseEntity<ErrorResponse> response =
          handler.handleOptimisticLockException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("RESOURCE_LOCKED");
    }
  }

  // =========================================================================
  // EmailSendingException handler
  // =========================================================================

  @Nested
  class EmailSendingExceptionTests {

    @Test
    void handleEmailSendingException_returns503() {
      EmailSendingException ex = new EmailSendingException("Email failed");

      ResponseEntity<ErrorResponse> response =
          handler.handleEmailSendingException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void handleEmailSendingException_hasEmailFailedCode() {
      EmailSendingException ex = new EmailSendingException("Email failed");

      ResponseEntity<ErrorResponse> response =
          handler.handleEmailSendingException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("EMAIL_FAILED");
    }
  }

  // =========================================================================
  // Generic exception catch-all handler
  // =========================================================================

  @Nested
  class GenericExceptionTests {

    @Test
    void handleGenericException_returns500() {
      Exception ex = new RuntimeException("Unexpected error");

      ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, webRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void handleGenericException_hasInternalServerErrorCode() {
      Exception ex = new RuntimeException("Unexpected");

      ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("INTERNAL_SERVER_ERROR");
    }

    @Test
    void handleGenericException_hasGenericMessage() {
      Exception ex = new RuntimeException("Some internal error details");

      ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, webRequest);

      // Should NOT expose internal error details
      assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
    }
  }

  // =========================================================================
  // Regression test: UNPROCESSABLE_CONTENT status code (changed from UNPROCESSABLE_ENTITY)
  // =========================================================================

  @Nested
  class UnprocessableContentRegressionTests {

    @Test
    void httpStatus_unprocessableContent_hasValue422() {
      // Regression: verify that UNPROCESSABLE_CONTENT maps to 422
      assertThat(HttpStatus.UNPROCESSABLE_CONTENT.value()).isEqualTo(422);
    }

    @Test
    void httpStatus_unprocessableContent_reasonPhrase_isUnprocessableContent() {
      // Regression: verify the new reason phrase matches UNPROCESSABLE_CONTENT
      // This verifies the change from "Unprocessable Entity" to "Unprocessable Content"
      assertThat(HttpStatus.UNPROCESSABLE_CONTENT.getReasonPhrase())
          .isEqualTo("Unprocessable Content");
    }

    @Test
    void handleMethodArgumentNotValidException_returns422UnprocessableContent() throws NoSuchMethodException {
      // Tests that MethodArgumentNotValidException returns HTTP 422 with UNPROCESSABLE_CONTENT
      // This is a regression test for the change from UNPROCESSABLE_ENTITY to UNPROCESSABLE_CONTENT

      Method method = Object.class.getMethod("toString");
      MethodParameter methodParameter = new MethodParameter(method, -1);

      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(new Object(), "request");
      bindingResult.addError(new FieldError("request", "teachingIds",
          "Array of teachingId is required"));

      MethodArgumentNotValidException ex =
          new MethodArgumentNotValidException(methodParameter, bindingResult);

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      // The HTTP status code must be 422
      assertThat(response.getStatusCode().value()).isEqualTo(422);
      // The body status field must also be 422
      assertThat(response.getBody().getStatus()).isEqualTo(422);
    }

    @Test
    void handleMethodArgumentNotValidException_responseBodyHasValidationFailedMessage()
        throws NoSuchMethodException {
      Method method = Object.class.getMethod("toString");
      MethodParameter methodParameter = new MethodParameter(method, -1);

      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(new Object(), "request");
      bindingResult.addError(new FieldError("request", "field", "must not be blank"));

      MethodArgumentNotValidException ex =
          new MethodArgumentNotValidException(methodParameter, bindingResult);

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
    }

    @Test
    void handleMethodArgumentNotValidException_responseBodyHasValidationErrorCode()
        throws NoSuchMethodException {
      Method method = Object.class.getMethod("toString");
      MethodParameter methodParameter = new MethodParameter(method, -1);

      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(new Object(), "request");
      bindingResult.addError(new FieldError("request", "field", "Invalid"));

      MethodArgumentNotValidException ex =
          new MethodArgumentNotValidException(methodParameter, bindingResult);

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    void handleMethodArgumentNotValidException_fieldErrorsIncludedInResponse()
        throws NoSuchMethodException {
      Method method = Object.class.getMethod("toString");
      MethodParameter methodParameter = new MethodParameter(method, -1);

      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(new Object(), "request");
      bindingResult.addError(new FieldError("request", "teachingIds",
          "Array of teachingId is required"));

      MethodArgumentNotValidException ex =
          new MethodArgumentNotValidException(methodParameter, bindingResult);

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      @SuppressWarnings("unchecked")
      java.util.Map<String, String> errors =
          (java.util.Map<String, String>) response.getBody().getErrors();
      assertThat(errors).containsKey("teachingIds");
      assertThat(errors.get("teachingIds")).isEqualTo("Array of teachingId is required");
    }

    @Test
    void handleMethodArgumentNotValidException_multipleFieldErrors_combinedWithSemicolon()
        throws NoSuchMethodException {
      Method method = Object.class.getMethod("toString");
      MethodParameter methodParameter = new MethodParameter(method, -1);

      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(new Object(), "request");
      // Two errors on the same field should be combined with ";"
      bindingResult.addError(new FieldError("request", "field", "Error one"));
      bindingResult.addError(new FieldError("request", "field", "Error two"));

      MethodArgumentNotValidException ex =
          new MethodArgumentNotValidException(methodParameter, bindingResult);

      ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

      @SuppressWarnings("unchecked")
      java.util.Map<String, String> errors =
          (java.util.Map<String, String>) response.getBody().getErrors();
      assertThat(errors.get("field")).contains(";");
    }
  }
}