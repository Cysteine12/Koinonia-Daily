package org.eni.koinoniadaily.modules.series;

import org.eni.koinoniadaily.modules.series.dto.SeriesRequest;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {
  
  public Series toEntity(SeriesRequest request) {

    return Series.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .build();
  }
}
