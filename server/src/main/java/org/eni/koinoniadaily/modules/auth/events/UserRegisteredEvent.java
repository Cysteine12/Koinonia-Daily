package org.eni.koinoniadaily.modules.auth.events;

public record UserRegisteredEvent(
    String email,
    String firstName
) {}
