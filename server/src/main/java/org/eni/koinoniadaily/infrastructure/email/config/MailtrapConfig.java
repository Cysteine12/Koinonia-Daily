package org.eni.koinoniadaily.infrastructure.email.config;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.config.AppProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ConditionalOnProperty(name = "app.email.provider", havingValue = "mailtrap")
@RequiredArgsConstructor
public class MailtrapConfig {

  private final AppProperties props;

  /**
   * Create a JavaMailSender configured for Mailtrap using values from application properties.
   *
   * @return a JavaMailSender configured with Mailtrap host, port, username, and password,
   *         and SMTP properties: protocol `smtp`, authentication enabled, STARTTLS enabled and required,
   *         and debug enabled.
   */
  @Bean
  public JavaMailSender javaMailSender() {

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(props.getMailtrap().getHost());
    mailSender.setPort(props.getMailtrap().getPort());
    mailSender.setUsername(props.getMailtrap().getUsername());
    mailSender.setPassword(props.getMailtrap().getPassword());

    Properties javaMailProperties = mailSender.getJavaMailProperties();
    javaMailProperties.put("mail.transport.protocol", "smtp");
    javaMailProperties.put("mail.smtp.auth", "true");
    javaMailProperties.put("mail.smtp.starttls.enable", "true");
    javaMailProperties.put("mail.smtp.starttls.required", "true");
    javaMailProperties.put("mail.debug", "true");

    return mailSender;
  }
}