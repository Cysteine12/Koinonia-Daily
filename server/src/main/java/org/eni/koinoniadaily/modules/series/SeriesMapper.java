package org.eni.koinoniadaily.modules.series;

import java.util.List;

import org.eni.koinoniadaily.modules.series.dto.SeriesRequest;
import org.eni.koinoniadaily.modules.series.dto.SeriesResponse;
import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {
  
  public Series toEntity(SeriesRequest request) {

    return Series.builder()
            .name(request.getName())
            .description(request.getDescription())
            .thumbnailUrl(request.getThumbnailUrl())
            .build();
  }

  public SeriesResponse toDto(Series series, List<TeachingWithoutMessageProjection> teachings) {

    return SeriesResponse.builder()
            .id(series.getId())
            .name(series.getName())
            .description(series.getDescription())
            .thumbnailUrl(series.getThumbnailUrl())
            .createdAt(series.getCreatedAt())
            .updatedAt(series.getUpdatedAt())
            .teachings(teachings)
            .build();
  }
}
