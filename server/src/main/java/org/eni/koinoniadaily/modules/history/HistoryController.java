package org.eni.koinoniadaily.modules.history;

import org.eni.koinoniadaily.modules.history.dto.HistoryRequest;
import org.eni.koinoniadaily.modules.history.dto.HistoryResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
@Validated
public class HistoryController {
  
  private final HistoryService historyService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<HistoryResponse>>> getHistoriesByUser(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    PageResponse<HistoryResponse> response = historyService.getHistoriesByUser(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PatchMapping("/{id}/marked-read")
  public ResponseEntity<SuccessResponse<Void>> updateHistoryMarkAsRead(
      @PathVariable @Positive Long id, 
      @Valid @RequestBody HistoryRequest request
  ) {
    historyService.updateHistoryMarkAsRead(id, request);

    return ResponseEntity.ok(SuccessResponse.message("History updated successfully"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteHistory(
      @PathVariable @Positive Long id
  ) {
    historyService.deleteHistory(id);

    return ResponseEntity.ok(SuccessResponse.message("History deleted successfully"));
  }
}
