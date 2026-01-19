package org.eni.koinoniadaily.modules.transcript.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscriptRequest {
  
  @NotBlank(message = "Title is required")
  @Size(max = 50, message = "Title cannot exceed 50 characters")
  private String title;

  @NotBlank(message = "Message is required")
  private String Message;
}
