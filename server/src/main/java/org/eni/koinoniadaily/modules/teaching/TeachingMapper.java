package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.modules.teaching.dto.TeachingRequest;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingResponse;
import org.springframework.stereotype.Component;

@Component
public class TeachingMapper {

  public TeachingResponse toDto(Teaching teaching) {

    return TeachingResponse.builder()
            .title(teaching.getTitle())
            .scripturalReferences(teaching.getScripturalReferences())
            .message(teaching.getMessage())
            .summary(teaching.getSummary())
            .audioUrl(teaching.getAudioUrl())
            .videoUrl(teaching.getVideoUrl())
            .thumbnailUrl(teaching.getThumbnailUrl())
            .type(teaching.getType())
            .tags(teaching.getTags())
            .series(teaching.getSeries())
            .seriesPart(teaching.getSeriesPart())
            .taughtAt(teaching.getTaughtAt())
            .build();
  }
  
  public Teaching toEntity(TeachingRequest dto) {

    return Teaching.builder()
            .title(dto.getTitle())
            .scripturalReferences(dto.getScripturalReferences())
            .message(dto.getMessage())
            .summary(dto.getSummary())
            .audioUrl(dto.getAudioUrl())
            .videoUrl(dto.getVideoUrl())
            .thumbnailUrl(dto.getThumbnailUrl())
            .type(dto.getType())
            .tags(dto.getTags())
            .series(dto.getSeries())
            .seriesPart(dto.getSeriesPart())
            .taughtAt(dto.getTaughtAt())
            .build();
  }

  public Teaching updateToEntity(Teaching teaching, TeachingRequest dto) {

    teaching.setTitle(dto.getTitle());
    teaching.setScripturalReferences(dto.getScripturalReferences());
    teaching.setMessage(dto.getMessage());
    teaching.setSummary(dto.getSummary());
    teaching.setAudioUrl(dto.getAudioUrl());
    teaching.setVideoUrl(dto.getVideoUrl());
    teaching.setThumbnailUrl(dto.getThumbnailUrl());
    teaching.setType(dto.getType());
    teaching.setTags(dto.getTags());
    teaching.setSeries(dto.getSeries());
    teaching.setSeriesPart(dto.getSeriesPart());
    teaching.setTaughtAt(dto.getTaughtAt());

    return teaching;
  }
}
