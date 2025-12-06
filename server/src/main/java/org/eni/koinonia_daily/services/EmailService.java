package org.eni.koinonia_daily.services;

import org.eni.koinonia_daily.config.AppProperties;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final SesClient sesClient;
  private final AppProperties props;
  private final TemplateEngine templateEngine;

  private void sendEmail(String to, String subject, String body) {
    Destination destination = Destination.builder()
                                .toAddresses(to)
                                .build();

    Message message = Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().html(Content.builder().data(body).build()).build())
                        .build();

    SendEmailRequest request = SendEmailRequest.builder()
                                .source(props.getAws().getSesFromEmail())
                                .destination(destination)
                                .message(message)
                                .build();

    sesClient.sendEmail(request);
  }

  public void sendEmailVerificationRequestMail(String email, String firstName, String otp) {

    Context context = new Context();
    context.setVariable("firstName", firstName);
    context.setVariable("otp", otp);
    context.setVariable("APP_NAME", props.getName());

    sendEmail(
      email,
      "Confirm your email address",
      templateEngine.process("confirm-email", context)
    );
  }

  public void sendWelcomeEmail(String email, String firstName) {
    
    Context context = new Context();
    context.setVariable("firstName", firstName);
    context.setVariable("APP_NAME", props.getName());

    sendEmail(
      email,
      "Welcome to " + props.getName(),
      templateEngine.process("welcome-email", context)
    );
  }

  public void sendForgotPasswordMail(String email, String otp) {
    
    Context context = new Context();
    context.setVariable("otp", otp);
    context.setVariable("APP_NAME", props.getName());

    sendEmail(
      email,
      props.getName() + " Account Password Reset",
      templateEngine.process("reset-password-email", context)
    );
  }

  public void sendPasswordChangedMail(String email, String firstName) {
    
    Context context = new Context();
    context.setVariable("firstName", firstName);
    context.setVariable("APP_NAME", props.getName());

    sendEmail(
      email,
      "Your account password was changed",
      templateEngine.process("change-password-email", context)
    );
  }
  
}
 