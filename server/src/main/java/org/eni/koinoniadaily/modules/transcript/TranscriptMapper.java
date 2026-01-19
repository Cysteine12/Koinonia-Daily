package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.modules.transcript.dto.TranscriptPageResponse;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptRequest;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptResponse;
import org.eni.koinoniadaily.modules.transcript.projection.TranscriptWithoutMessageProjection;
import org.springframework.stereotype.Component;

@Component
public class TranscriptMapper {
  
  public TranscriptPageResponse toDto(TranscriptWithoutMessageProjection transcript) {
    
    return TranscriptPageResponse.builder()
            .id(transcript.getId())
            .title(transcript.getTitle())
            .createdAt(transcript.getCreatedAt())
            .updatedAt(transcript.getUpdatedAt())
            .build();
  }

  public TranscriptResponse toDto(Transcript transcript) {
    
    return TranscriptResponse.builder()
            .id(transcript.getId())
            .title(transcript.getTitle())
            .message(transcript.getMessage())
            .createdAt(transcript.getCreatedAt())
            .updatedAt(transcript.getUpdatedAt())
            .build();
  }

  public Transcript toEntity(TranscriptRequest request) {
    
    return Transcript.builder()
            .title(request.getTitle())
            .message(request.getMessage())
            .build();
  }  
}
