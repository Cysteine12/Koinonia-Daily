package org.eni.koinoniadaily.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class RegisterRequest {
  
  @NotBlank(message = "Firstname is required")
  @Size(max = 50, message = "Firstname length cannot exceed 50 characters")
  private String firstName;

  @NotBlank(message = "Lastname is required")
  @Size(max = 50, message = "Lastname length cannot exceed 50 characters")
  private String lastName;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  @Size(max = 100, message = "Email length cannot exceed 100 characters")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 72, message = "Minimum password length of 8 required")
  private String password;
}
