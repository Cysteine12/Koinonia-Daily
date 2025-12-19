package org.eni.koinonia_daily.modules.auth;

import org.eni.koinonia_daily.modules.auth.events.EmailVerificationRequestedEvent;
import org.eni.koinonia_daily.modules.auth.events.PasswordChangedEvent;
import org.eni.koinonia_daily.modules.auth.events.PasswordResetOtpGeneratedEvent;
import org.eni.koinonia_daily.modules.auth.events.UserRegisteredEvent;
import org.eni.koinonia_daily.services.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthEventListener {
  
  private final EmailService emailService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onEmailVerificationRequested(EmailVerificationRequestedEvent event) {
    emailService.sendEmailVerificationRequestMail(event.getEmail(), event.getFirstName(), event.getOtp());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserRegistered(UserRegisteredEvent event) {
    emailService.sendWelcomeEmail(event.getEmail(), event.getFirstName());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onPasswordResetOtpGenerated(PasswordResetOtpGeneratedEvent event) {
    emailService.sendForgotPasswordMail(event.getEmail(), event.getOtp());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onPasswordChanged(PasswordChangedEvent event) {
    emailService.sendForgotPasswordMail(event.getEmail(), event.getFirstName());
  }
}
