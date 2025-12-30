package org.eni.koinoniadaily.modules.auth;

import org.eni.koinoniadaily.modules.auth.events.EmailVerificationRequestedEvent;
import org.eni.koinoniadaily.modules.auth.events.PasswordChangedEvent;
import org.eni.koinoniadaily.modules.auth.events.PasswordResetOtpGeneratedEvent;
import org.eni.koinoniadaily.modules.auth.events.UserRegisteredEvent;
import org.eni.koinoniadaily.services.EmailService;
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
    emailService.sendEmailVerificationRequestMail(event.email(), event.firstName(), event.otp());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserRegistered(UserRegisteredEvent event) {
    emailService.sendWelcomeEmail(event.email(), event.firstName());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onPasswordResetOtpGenerated(PasswordResetOtpGeneratedEvent event) {
    emailService.sendForgotPasswordMail(event.email(), event.otp());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onPasswordChanged(PasswordChangedEvent event) {
    emailService.sendPasswordChangedMail(event.email(), event.firstName());
  }
}
