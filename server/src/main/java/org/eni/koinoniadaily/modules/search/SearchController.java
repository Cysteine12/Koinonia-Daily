package org.eni.koinoniadaily.modules.search;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.modules.search.dto.SearchResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@Validated
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  @GetMapping
  public ResponseEntity<SuccessResponse<List<SearchResponse>>> search(
      @RequestParam @NotBlank String query,
      @RequestParam(defaultValue = "0") @PositiveOrZero @Max(20) int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size,
      @RequestParam(defaultValue = "false") boolean semantic
  ) {
    List<SearchResponse> results = searchService.search(query, page, size, semantic);

    return ResponseEntity.ok(SuccessResponse.data(results));
  }
}
