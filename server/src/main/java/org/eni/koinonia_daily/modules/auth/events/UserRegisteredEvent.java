package org.eni.koinonia_daily.modules.auth.events;

public record UserRegisteredEvent (
  String email,
  String firstName
) {}
