package org.eni.koinoniadaily.modules.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountProfileRequest {
  
  @NotBlank(message = "Firstname is required")
  @Size(max = 50, message = "Firstname cannot exceed 50 characters")
  private String firstName;
  
  @NotBlank(message = "Lastname is required")
  @Size(max = 50, message = "Lastname cannot exceed 50 characters")
  private String lastName;
  
  @Size(max = 100, message = "Photo URL cannot exceed 100 characters")
  private String photoUrl;
}
