package org.eni.koinoniadaily.modules.search;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

import org.eni.koinoniadaily.modules.search.dto.SearchClickRequest;
import org.eni.koinoniadaily.modules.search.dto.SearchClickResponse;
import org.eni.koinoniadaily.modules.search.dto.SearchResponse;
import org.eni.koinoniadaily.modules.search.dto.SearchSuggestionResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/suggestions")
  public ResponseEntity<SuccessResponse<List<SearchSuggestionResponse>>> suggest(      
      @RequestParam @NotBlank String query,
      @RequestParam(defaultValue = "0") @PositiveOrZero @Max(20) int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    List<SearchSuggestionResponse> results = searchService.suggest(query, page, size);

    return ResponseEntity.ok(SuccessResponse.data(results));
  }

  @GetMapping("/recent")
  public ResponseEntity<SuccessResponse<PageResponse<SearchClickResponse>>> getRecentClicks(
      @RequestParam(defaultValue = "0") @PositiveOrZero @Max(20) int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    PageResponse<SearchClickResponse> response = searchService.getRecentClicks(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping("/clicks")
  public ResponseEntity<SuccessResponse<Void>> recordClick(
      @RequestBody @Valid SearchClickRequest request
  ) {
    searchService.recordClick(request);

    return ResponseEntity.ok(SuccessResponse.message("Search click recorded successfully"));
  }
}
