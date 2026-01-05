package org.eni.koinoniadaily.modules.bookmarkcategory;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.auth.CurrentUserProvider;
import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryRequest;
import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkCategoryService {
  
  private final BookmarkCategoryRepository bookmarkCategoryRepository;
  private final BookmarkCategoryMapper bookmarkCategoryMapper;
  private final UserRepository userRepository;
  private final CurrentUserProvider currentUserProvider;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<BookmarkCategoryResponse> getBookmarkCategoryByUser(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Long userId = currentUserProvider.getCurrentUserId();

    Page<BookmarkCategoryResponse> categories = bookmarkCategoryRepository.findAllByUserId(userId, pageable);

    return PageResponse.from(categories);
  }

  public BookmarkCategoryResponse getBookmarkCategoryById(Long id) {
    
    Long userId = currentUserProvider.getCurrentUserId();

    return bookmarkCategoryRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException("Bookmark category not found"));
  }

  @Transactional
  public BookmarkCategoryResponse createBookmarkCategory(BookmarkCategoryRequest request) {

    Long userId = currentUserProvider.getCurrentUserId();

    User user = userRepository.getReferenceById(userId);

    BookmarkCategory category = bookmarkCategoryMapper.toEntity(request.getName(), user);

    BookmarkCategory savedCategory = bookmarkCategoryRepository.save(category);

    return bookmarkCategoryMapper.toDto(savedCategory);
  }

  @Transactional
  public void updateBookmarkCategoryName(Long id, BookmarkCategoryRequest request) {

    Long userId = currentUserProvider.getCurrentUserId();

    BookmarkCategory category = bookmarkCategoryRepository.findById(id)
                                  .orElseThrow(() -> new NotFoundException("Bookmark category not found"));

    if (!category.getUser().getId().equals(userId)) {
      throw new NotFoundException("Bookmark category not found");
    }
    
    category.setName(request.getName());
  }

  @Transactional
  public void deleteBookmarkCategory(Long id) {

    Long userId = currentUserProvider.getCurrentUserId();

    BookmarkCategory category = bookmarkCategoryRepository.findById(id)
                                  .orElseThrow(() -> new NotFoundException("Bookmark category not found"));

    if (!category.getUser().getId().equals(userId)) {
      throw new NotFoundException("Bookmark category not found");
    }
    
    // By deleting a managed entity, JPA will handle cascading to associated bookmarks as expected behaviour.
    bookmarkCategoryRepository.delete(category);
  }
}
