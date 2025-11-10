package org.eni.koinonia_daily.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
  @NotBlank(message = "Firstname is required")
  private String firstName;

  @NotBlank(message = "Lastname is required")
  private String lastName;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 7, message = "Minimum password length of 7 required")
  private String password;
}
