package org.eni.koinoniadaily.infrastructure.email.providers;

public interface EmailProvider {
  void send(String to, String subject, String body);
}
