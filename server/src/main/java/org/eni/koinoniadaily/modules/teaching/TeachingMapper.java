package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.modules.series.Series;
import org.eni.koinoniadaily.modules.series.dto.SeriesSummary;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingRequest;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingResponse;
import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.springframework.stereotype.Component;

@Component
public class TeachingMapper {

  public TeachingPageResponse toDto(TeachingWithoutMessageProjection teaching) {

    return TeachingPageResponse.builder()
            .id(teaching.getId())
            .title(teaching.getTitle())
            .scripturalReferences(teaching.getScripturalReferences())
            .summary(teaching.getSummary())
            .audioUrl(teaching.getAudioUrl())
            .videoUrl(teaching.getVideoUrl())
            .thumbnailUrl(teaching.getThumbnailUrl())
            .type(teaching.getType())
            .tags(teaching.getTags())
            .seriesPart(teaching.getSeriesPart())
            .taughtAt(teaching.getTaughtAt())
            .createdAt(teaching.getCreatedAt())
            .updatedAt(teaching.getUpdatedAt())
            .build();
  }

  public TeachingResponse toDto(Teaching teaching) {

    SeriesSummary series = teaching.getSeries() != null 
                            ? SeriesSummary.builder()
                                .id(teaching.getSeries().getId())
                                .name(teaching.getSeries().getName())
                                .build() 
                            : null;

    return TeachingResponse.builder()
            .id(teaching.getId())
            .title(teaching.getTitle())
            .scripturalReferences(teaching.getScripturalReferences())
            .message(teaching.getMessage())
            .summary(teaching.getSummary())
            .audioUrl(teaching.getAudioUrl())
            .videoUrl(teaching.getVideoUrl())
            .thumbnailUrl(teaching.getThumbnailUrl())
            .type(teaching.getType())
            .tags(teaching.getTags())
            .series(series)
            .seriesPart(teaching.getSeriesPart())
            .taughtAt(teaching.getTaughtAt())
            .createdAt(teaching.getCreatedAt())
            .updatedAt(teaching.getUpdatedAt())
            .build();
  }
  
  public Teaching toEntity(TeachingRequest dto, Series series) {

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
            .series(series)
            .seriesPart(dto.getSeriesPart())
            .taughtAt(dto.getTaughtAt())
            .build();
  }

  public Teaching updateToEntity(Teaching teaching, TeachingRequest dto, Series series) {

    teaching.setTitle(dto.getTitle());
    teaching.setScripturalReferences(dto.getScripturalReferences());
    teaching.setMessage(dto.getMessage());
    teaching.setSummary(dto.getSummary());
    teaching.setAudioUrl(dto.getAudioUrl());
    teaching.setVideoUrl(dto.getVideoUrl());
    teaching.setThumbnailUrl(dto.getThumbnailUrl());
    teaching.setType(dto.getType());
    teaching.setTags(dto.getTags());
    teaching.setSeries(series);
    teaching.setSeriesPart(dto.getSeriesPart());
    teaching.setTaughtAt(dto.getTaughtAt());

    return teaching;
  }
}
