package org.eni.koinoniadaily.modules.series;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.series.dto.SeriesPageResponse;
import org.eni.koinoniadaily.modules.series.dto.SeriesRequest;
import org.eni.koinoniadaily.modules.series.dto.SeriesResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeriesService {
  
  private final SeriesRepository seriesRepository;
  private final SeriesMapper seriesMapper;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<SeriesPageResponse> getSeries(int page, int size) {

    Pageable pageable =  PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Page<SeriesPageResponse> series = seriesRepository.findAllBy(pageable);
    
    return PageResponse.from(series);
  }

  public SeriesResponse getSeriesById(Long id) {

    return seriesRepository.findProjectedById(id)
            .orElseThrow(() -> new NotFoundException("Series not found"));
  }

  @Transactional
  public SeriesResponse createSeries(SeriesRequest request) {

    Series series = seriesRepository.save(seriesMapper.toEntity(request));

    return getSeriesById(series.getId());
  }

  @Transactional
  public void updateSeries(Long id, SeriesRequest request) {

    Series series = seriesRepository.findById(id)
                      .orElseThrow(() -> new NotFoundException("Series not found"));

    series.setTitle(request.getTitle());
    series.setDescription(request.getDescription());
  }
  
  @Transactional
  public void deleteSeries(Long id) {

    Series series = seriesRepository.findById(id)
                      .orElseThrow(() -> new NotFoundException("Series not found"));

    // By deleting a managed entity, JPA will handle cascading to associated teachings as expected behaviour.
    seriesRepository.delete(series);
  }
}
