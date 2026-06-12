package org.eni.koinoniadaily.modules.transcript.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscriptRequest {
  
  @NotBlank(message = "Title is required")
  @Size(max = 60, message = "Title cannot exceed 60 characters")
  private String title;

  private String message;
}
