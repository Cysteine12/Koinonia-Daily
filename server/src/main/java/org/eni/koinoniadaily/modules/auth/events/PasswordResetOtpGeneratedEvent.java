package org.eni.koinoniadaily.modules.auth.events;

public record PasswordResetOtpGeneratedEvent(
    String email,
    String otp
) {}
