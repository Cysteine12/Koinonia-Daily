package org.eni.koinoniadaily.modules.transcript.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptTriggerRequest {

  @NotNull(message = "transcriptId is required")
  @Positive(message = "transcriptId must be greater than zero")
  private Long transcriptId;

  @NotBlank(message ="audioUrl is required")
  @URL(protocol = "https", message = "audioUrl must be valid https url")
  private String audioUrl;
}
