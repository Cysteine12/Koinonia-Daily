package org.eni.koinoniadaily.modules.series;

import java.util.List;

import org.eni.koinoniadaily.modules.series.dto.SeriesRequest;
import org.eni.koinoniadaily.modules.series.dto.SeriesResponse;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {
  
  public Series toEntity(SeriesRequest request) {

    return Series.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .build();
  }

  public SeriesResponse toDto(Series series, List<TeachingPageResponse> teachings) {

    return SeriesResponse.builder()
            .id(series.getId())
            .title(series.getTitle())
            .description(series.getDescription())
            .teachings(teachings)
            .build();
  }
}
