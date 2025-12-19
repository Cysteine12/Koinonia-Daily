package org.eni.koinonia_daily.modules.auth.events;

public record PasswordChangedEvent  (
  String email,
  String firstName
) {}
