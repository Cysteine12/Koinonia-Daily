package org.eni.koinoniadaily.modules.bookmarkcategory;

import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryRequest;
import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
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
    PageResponse<BookmarkCategoryResponse> bookmarkCategories = 
        bookmarkCategoryService.getBookmarkCategoryByUser(page, size);

    return ResponseEntity.ok(SuccessResponse.data(bookmarkCategories));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<BookmarkCategoryResponse>> getBookmarkCategoryById(@PathVariable Long id) {

    BookmarkCategoryResponse bookmarkCategory = bookmarkCategoryService.getBookmarkCategoryById(id);

    return ResponseEntity.ok(SuccessResponse.data(bookmarkCategory));
  }

  @PostMapping
  public ResponseEntity<SuccessResponse<BookmarkCategoryResponse>> createBookmarkCategory(
      @RequestBody @Valid BookmarkCategoryRequest payload
  ) {
    BookmarkCategoryResponse bookmarkCategory = bookmarkCategoryService.createBookmarkCategory(payload);

    return ResponseEntity.ok(SuccessResponse.data(bookmarkCategory));
  }

  @PatchMapping("/{id}/name")
  public ResponseEntity<SuccessResponse<Void>> updateBookmarkCategoryName(
      @PathVariable Long id,
      @RequestBody @Valid BookmarkCategoryRequest payload
  ) {
    bookmarkCategoryService.updateBookmarkCategoryName(id, payload);

    return ResponseEntity.ok(SuccessResponse.message("Bookmark category name updated successfully"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteBookmarkCategory(@PathVariable Long id) {

    bookmarkCategoryService.deleteBookmarkCategory(id);

    return ResponseEntity.ok(SuccessResponse.message("Bookmark category deleted successfully"));
  }
}
