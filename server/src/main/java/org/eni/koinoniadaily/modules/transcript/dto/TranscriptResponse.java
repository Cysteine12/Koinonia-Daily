package org.eni.koinoniadaily.modules.transcript.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranscriptResponse {

  private Long id;
  
  private String title;
  
  private String message;
  
  private Instant createdAt;

  private Instant updatedAt;
}