package org.eni.koinonia_daily.modules.auth.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordChangedEvent {
  
  private final String email;

  private final String firstName;
}
