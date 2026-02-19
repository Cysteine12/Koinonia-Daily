package org.eni.koinoniadaily.infrastructure.email.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.exceptions.EmailSendingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.email.provider", havingValue = "ses", matchIfMissing = true)
@Qualifier("ses")
@RequiredArgsConstructor
public class SesEmailProvider implements EmailProvider {

  private final SesClient sesClient;
  private final AppProperties props;

  @Override
  public void send(String to, String subject, String body) {
    try {
      Destination destination = Destination.builder()
              .toAddresses(to)
              .build();

      Message message = Message.builder()
              .subject(Content.builder().data(subject).build())
              .body(Body.builder().html(Content.builder().data(body).build()).build())
              .build();

      SendEmailRequest request = SendEmailRequest.builder()
              .source(props.getEmail().getFrom())
              .destination(destination)
              .message(message)
              .build();

      sesClient.sendEmail(request);
      log.info("Email sent successfully via SES");
    } catch (SesException ex) {
      log.error("Failed to send email via SES to {}", to, ex);
      throw new EmailSendingException("Failed to send email via SES", ex);
    }
  }
}
