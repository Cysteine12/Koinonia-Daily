package org.eni.koinoniadaily.modules.bookmark;

import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkRequest;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkResponse;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkUpdateRequest;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Validated
public class BookmarkController {
  
  private final BookmarkService bookmarkService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<BookmarkResponse>>> getBookingsByCategory(
      @RequestParam Long categoryId,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "50") @Max(100) int size
  ) {
    PageResponse<BookmarkResponse> response = bookmarkService.getBookmarksByCategory(categoryId, page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  public ResponseEntity<SuccessResponse<Void>> createBookmark(@Valid BookmarkRequest request) {

    bookmarkService.createBookmark(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.message("Bookmark created successfully"));
  }

  @PatchMapping("/{id}/note")
  public ResponseEntity<SuccessResponse<Void>> updateBookmarkNote(
      @PathVariable @NotNull @Positive Long id, 
      @Valid BookmarkUpdateRequest request
  ) {
    bookmarkService.updateBookmarkNote(id, request);

    return ResponseEntity.ok(SuccessResponse.message("Bookmark updated successfully"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteBookmark(@PathVariable @NotNull @Positive Long id) {

    bookmarkService.deleteBookmark(id);

    return ResponseEntity.ok(SuccessResponse.message("Bookmark deleted successfully"));
  }
}
