package org.eni.koinoniadaily.modules.history;

import org.eni.koinoniadaily.modules.history.dto.HistoryRequest;
import org.eni.koinoniadaily.modules.history.dto.HistoryResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryContoller {
  
  private final HistoryService historyService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<HistoryResponse>>> getHistoriesByUser(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size
  ) {
    PageResponse<HistoryResponse> histories = historyService.getHistoriesByUser(page, size);

    return ResponseEntity.ok(SuccessResponse.data(histories));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<SuccessResponse<History>> updateHistoryMarkAsRead(@PathVariable Long id, @RequestBody HistoryRequest payload) {
    
    History history = historyService.updateHistoryMarkAsRead(id, payload);

    return ResponseEntity.ok(SuccessResponse.of(history, "History updated successfully"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteHistory(@PathVariable Long id) {
    
    historyService.deleteHistory(id);

    return ResponseEntity.ok(SuccessResponse.message("History updated successfully"));
  }
}
