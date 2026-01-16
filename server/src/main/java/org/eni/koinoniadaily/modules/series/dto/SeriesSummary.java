package org.eni.koinoniadaily.modules.series.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeriesSummary {
  
  private Long id;

  private String name;
}
