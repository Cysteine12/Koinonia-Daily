package org.eni.koinoniadaily.infrastucture.email.providers;

public interface EmailProvider {
  void send (String to, String subject, String body);
}
