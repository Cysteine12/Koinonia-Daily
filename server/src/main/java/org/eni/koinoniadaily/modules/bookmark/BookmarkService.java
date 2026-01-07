package org.eni.koinoniadaily.modules.bookmark;


import java.util.Optional;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.modules.auth.CurrentUserProvider;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkRequest;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkResponse;
import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkUpdateRequest;
import org.eni.koinoniadaily.modules.bookmarkcategory.BookmarkCategory;
import org.eni.koinoniadaily.modules.bookmarkcategory.BookmarkCategoryRepository;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRepository;
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
  private final BookmarkCategoryRepository bookmarkCategoryRepository;
  private final TeachingRepository teachingRepository;
  private final UserRepository userRepository;
  private final BookmarkMapper bookmarkMapper;
  private final CurrentUserProvider currentUserProvider;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<BookmarkResponse> getBookmarksByCategory(Long categoryId, int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Long userId = currentUserProvider.getCurrentUserId();

    Page<BookmarkResponse> bookmarks = bookmarkRepository.findAllByCategoryIdAndUserId(categoryId, userId, pageable);

    return PageResponse.from(bookmarks);
  }

  @Transactional
  public void createBookmark(BookmarkRequest request) {

    Long userId = currentUserProvider.getCurrentUserId();

    Optional<Bookmark> bookmark = bookmarkRepository.findByUserIdAndCategoryIdAndTeachingId(userId, request.getCategoryId(), request.getTeachingId());

    if (bookmark.isPresent()) {
      throw new ValidationException("Teaching already added to the category");
    }

    User user = userRepository.getReferenceById(userId);

    Teaching teaching = teachingRepository.findById(request.getTeachingId())
                          .orElseThrow(() -> new NotFoundException("Teaching not found"));

    BookmarkCategory category = bookmarkCategoryRepository.findByIdAndUser_Id(request.getCategoryId(), userId)
                                  .orElseThrow(() -> new NotFoundException("Category not found"));

    bookmarkRepository.save(bookmarkMapper.toEntity(user, category, teaching, request.getNote()));
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

    Bookmark bookmark = bookmarkRepository.findByIdAndUserId(id, userId)
                          .orElseThrow(() -> new NotFoundException("Bookmark not found"));

    bookmarkRepository.delete(bookmark);
  }
}
