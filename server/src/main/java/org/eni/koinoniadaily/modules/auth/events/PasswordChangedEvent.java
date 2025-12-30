package org.eni.koinoniadaily.modules.auth.events;

public record PasswordChangedEvent(
    String email,
    String firstName
) {}
