package org.eni.koinoniadaily.modules.transcript.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranscriptPageResponse {

  private Long id;

  private String title;
  
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}