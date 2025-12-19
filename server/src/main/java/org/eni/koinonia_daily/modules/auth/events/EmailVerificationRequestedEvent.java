package org.eni.koinonia_daily.modules.auth.events;

public record EmailVerificationRequestedEvent (
  String email,
  String firstName,
  String otp
) {}
