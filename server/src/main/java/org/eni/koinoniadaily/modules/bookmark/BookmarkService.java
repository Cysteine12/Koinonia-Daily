package org.eni.koinoniadaily.modules.bookmark;


import java.util.Optional;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.modules.auth.CurrentUserProvider;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkRequest;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkResponse;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkUpdateRequest;
import org.eni.koinoniadaily.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {
  
  private final BookmarkRepository bookmarkRepository;
  private final BookmarkMapper bookmarkMapper;
  private final CurrentUserProvider currentUserProvider;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<BookmarkResponse> getBookmarksByCategory(Long id, int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Long userId = currentUserProvider.getCurrentUserId();

    Page<BookmarkResponse> bookmarks = bookmarkRepository.findAllByCategoryIdAndUserId(id, userId, pageable);

    return PageResponse.from(bookmarks);
  }

  @Transactional
  public void createBookmark(BookmarkRequest request) {

    Long userId = currentUserProvider.getCurrentUserId();

    Optional<Bookmark> bookmark = bookmarkRepository.findByUserIdAndCategoryIdAndTeachingId(userId, request.getCategoryId(), request.getTeachingId());

    if (bookmark.isPresent()) {
      throw new ValidationException("Teaching already added to the category");
    }

    bookmarkRepository.save(bookmarkMapper.toEntity(userId, request));
  }

  @Transactional
  public void updateBookmarkNote(Long id, BookmarkUpdateRequest request) {

    Long userId = currentUserProvider.getCurrentUserId();

    Bookmark bookmark = bookmarkRepository.findByIdAndUserId(id, userId)
                        .orElseThrow(() -> new NotFoundException("Bookmark not found"));
              
    bookmark.setNote(request.getNote());
  }

  @Transactional
  public void deleteBookmark(Long id) {

    Long userId = currentUserProvider.getCurrentUserId();

    boolean bookmarkExists = bookmarkRepository.existsByIdAndUserId(id, userId);

    if (!bookmarkExists) {
      throw new NotFoundException("Bookmark not found");
    }

    bookmarkRepository.deleteById(id);
  }
}
