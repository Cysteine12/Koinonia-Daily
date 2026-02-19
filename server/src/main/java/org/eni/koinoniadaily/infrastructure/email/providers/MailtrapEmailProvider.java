package org.eni.koinoniadaily.infrastructure.email.providers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.exceptions.EmailSendingException;
import org.eni.koinoniadaily.config.AppProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.email.provider", havingValue = "mailtrap")
@Qualifier("mailtrap")
@RequiredArgsConstructor
public class MailtrapEmailProvider implements EmailProvider {

  private final JavaMailSender mailSender;
  private final AppProperties props;

  /**
   * Sends an HTML email to a single recipient using the configured Mailtrap JavaMailSender.
   *
   * @param to the recipient's email address
   * @param subject the email subject line
   * @param body the email body as HTML content
   * @throws EmailSendingException if sending fails due to an underlying messaging or mail exception
   */
  @Override
  public void send(String to, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(props.getEmail().getFrom());
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);

      mailSender.send(message);
      log.info("Email sent successfully via Mailtrap");
    } catch (MessagingException | MailException ex) {
      log.error("Failed to send email via Mailtrap to {}", to, ex);
      throw new EmailSendingException("Failed to send email via Mailtrap", ex);
    }
  }
}