package org.eni.koinoniadaily.infrastructure.email;

import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.infrastructure.email.providers.EmailProvider;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final EmailProvider emailProvider;
  private final AppProperties props;
  private final TemplateEngine templateEngine;

  /**
   * Sends an email prompting the recipient to confirm their email address.
   *
   * Renders the "confirm-email" template with the variables `firstName`, `otp`, and `APP_NAME` (from application properties)
   * and sends it using the configured email provider with the subject "Confirm your email address".
   *
   * @param email     the recipient's email address
   * @param firstName the recipient's first name used to personalize the message
   * @param otp       the one-time verification code included in the message
   */
  public void sendEmailVerificationRequestMail(String email, String firstName, String otp) {

    Context context = new Context();
    context.setVariable("firstName", firstName);
    context.setVariable("otp", otp);
    context.setVariable("APP_NAME", props.getName());

    emailProvider.send(
        email,
        "Confirm your email address",
        templateEngine.process("confirm-email", context)
    );
  }

  /**
   * Sends a welcome email to the specified recipient.
   *
   * The message uses the "welcome-email" template and includes the recipient's first name
   * and the application name as template variables; the subject is "Welcome to {APP_NAME}".
   *
   * @param email     the recipient's email address
   * @param firstName the recipient's first name used in the email template
   */
  public void sendWelcomeEmail(String email, String firstName) {
    
    Context context = new Context();
    context.setVariable("firstName", firstName);
    context.setVariable("APP_NAME", props.getName());

    emailProvider.send(
        email,
        "Welcome to " + props.getName(),
        templateEngine.process("welcome-email", context)
    );
  }

  /**
   * Sends a password reset email containing a one-time password to the specified address.
   *
   * @param email the recipient's email address
   * @param otp   the one-time password token to include in the reset email
   */
  public void sendForgotPasswordMail(String email, String otp) {
    
    Context context = new Context();
    context.setVariable("otp", otp);
    context.setVariable("APP_NAME", props.getName());

    emailProvider.send(
        email,
        props.getName() + " Account Password Reset",
        templateEngine.process("reset-password-email", context)
    );
  }

  /**
   * Sends a password-changed notification email to the specified recipient using the "change-password-email" template.
   *
   * @param email recipient's email address
   * @param firstName recipient's first name used to personalize the message
   */
  public void sendPasswordChangedMail(String email, String firstName) {
    
    Context context = new Context();
    context.setVariable("firstName", firstName);
    context.setVariable("APP_NAME", props.getName());

    emailProvider.send(
        email,
        "Your account password was changed",
        templateEngine.process("change-password-email", context)
    );
  }
  
}
 