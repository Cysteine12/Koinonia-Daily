package org.eni.koinoniadaily.modules.auth.events;

public record EmailVerificationRequestedEvent(
    String email,
    String firstName,
    String otp
) {}
