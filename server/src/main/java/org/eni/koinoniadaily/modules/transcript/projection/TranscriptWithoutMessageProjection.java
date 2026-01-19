package org.eni.koinoniadaily.modules.transcript.projection;

import java.time.LocalDateTime;

public interface TranscriptWithoutMessageProjection {

  Long getId();

  String getTitle();
  
  LocalDateTime getCreatedAt();
  
  LocalDateTime getUpdatedAt();  
}
