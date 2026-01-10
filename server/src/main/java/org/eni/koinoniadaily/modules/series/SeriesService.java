package org.eni.koinoniadaily.modules.series;

import java.util.List;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.series.dto.SeriesPageResponse;
import org.eni.koinoniadaily.modules.series.dto.SeriesRequest;
import org.eni.koinoniadaily.modules.series.dto.SeriesResponse;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;
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
  private final TeachingRepository teachingRepository;
  private final SeriesMapper seriesMapper;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<SeriesPageResponse> getSeries(int page, int size) {

    Pageable pageable =  PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Page<SeriesPageResponse> series = seriesRepository.findAllBy(pageable);
    
    return PageResponse.from(series);
  }

  public SeriesResponse getSeriesById(Long id) {

    Series series = seriesRepository.findById(id)
                      .orElseThrow(() -> new NotFoundException("Series not found"));

    List<TeachingPageResponse> teachings = teachingRepository.findAllBySeriesId(id);

    return seriesMapper.toDto(series, teachings);
  }

  @Transactional
  public SeriesResponse createSeries(SeriesRequest request) {

    Series series = seriesRepository.save(seriesMapper.toEntity(request));

    return seriesMapper.toDto(series, null);
  }

  @Transactional
  public SeriesResponse updateSeries(Long id, SeriesRequest request) {

    Series series = seriesRepository.findById(id)
                      .orElseThrow(() -> new NotFoundException("Series not found"));

    series.setTitle(request.getTitle());
    series.setDescription(request.getDescription());

    return seriesMapper.toDto(series, null);
  }
  
  @Transactional
  public void deleteSeries(Long id) {

    Series series = seriesRepository.findById(id)
                      .orElseThrow(() -> new NotFoundException("Series not found"));

    // By deleting a managed entity, JPA will handle cascading to associated teachings as expected behaviour.
    seriesRepository.delete(series);
  }
}
