package org.eni.koinoniadaily.modules.bookmarkcategory;

import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryRequest;
import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookmark-categories")
@RequiredArgsConstructor
public class BookmarkCategoryController {
  
  private final BookmarkCategoryService bookmarkCategoryService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<BookmarkCategoryResponse>>> getBookmarkCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    PageResponse<BookmarkCategoryResponse> response = 
        bookmarkCategoryService.getBookmarkCategoriesByUser(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<BookmarkCategoryResponse>> getBookmarkCategoryById(@PathVariable Long id) {

    BookmarkCategoryResponse response = bookmarkCategoryService.getBookmarkCategoryById(id);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  public ResponseEntity<SuccessResponse<BookmarkCategoryResponse>> createBookmarkCategory(
      @RequestBody @Valid BookmarkCategoryRequest request
  ) {
    BookmarkCategoryResponse response = bookmarkCategoryService.createBookmarkCategory(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.data(response));
  }

  @PatchMapping("/{id}/name")
  public ResponseEntity<SuccessResponse<Void>> updateBookmarkCategoryName(
      @PathVariable Long id,
      @RequestBody @Valid BookmarkCategoryRequest request
  ) {
    bookmarkCategoryService.updateBookmarkCategoryName(id, request);

    return ResponseEntity.ok(SuccessResponse.message("Bookmark category name updated successfully"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteBookmarkCategory(@PathVariable Long id) {

    bookmarkCategoryService.deleteBookmarkCategory(id);

    return ResponseEntity.ok(SuccessResponse.message("Bookmark category deleted successfully"));
  }
}
