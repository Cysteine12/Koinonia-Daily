package org.eni.koinoniadaily.modules.transcript.dto;

import org.eni.koinoniadaily.modules.transcript.TranscriptStatus;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranscriptPageResponse {

  private Long id;

  private String title;

  private TranscriptStatus status;
  
  private Instant createdAt;

  private Instant updatedAt;
}