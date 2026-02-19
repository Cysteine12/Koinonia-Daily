package org.eni.koinoniadaily.infrastructure.email.providers;

public interface EmailProvider {
  /**
 * Sends an email message to the specified recipient with the given subject and body.
 *
 * @param to      recipient email address
 * @param subject subject line of the email
 * @param body    content of the email
 */
void send(String to, String subject, String body);
}