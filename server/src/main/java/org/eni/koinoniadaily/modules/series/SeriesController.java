package org.eni.koinoniadaily.modules.series;

import org.eni.koinoniadaily.modules.series.dto.SeriesPageResponse;
import org.eni.koinoniadaily.modules.series.dto.SeriesRequest;
import org.eni.koinoniadaily.modules.series.dto.SeriesResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
@Validated
public class SeriesController {
  
  private final SeriesService seriesService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<SeriesPageResponse>>> getSeries(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @Positive int size
  ) {
    PageResponse<SeriesPageResponse> response = seriesService.getSeries(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<SeriesResponse>> getSeriesById(
      @PathVariable @Positive Long id
  ) {
    SeriesResponse response = seriesService.getSeriesById(id);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<SeriesResponse>> createSeries(
      @RequestBody @Valid SeriesRequest request
  ) {
    SeriesResponse response = seriesService.createSeries(request);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessResponse.of(response, "Series created successfully"));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<SeriesResponse>> updateSeries(
    @PathVariable @Positive Long id,
    @RequestBody @Valid SeriesRequest request
  ) {
    SeriesResponse response = seriesService.updateSeries(id, request);

    return ResponseEntity.ok(SuccessResponse.of(response, "Series updated successfully"));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> deleteSeries(
    @PathVariable @Positive Long id
  ) {
    seriesService.deleteSeries(id);

    return ResponseEntity.ok(SuccessResponse.message("Series deleted successfully"));
  }
}
