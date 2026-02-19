package org.eni.koinoniadaily.infrastucture.email;

import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.infrastucture.email.providers.EmailProvider;
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
 