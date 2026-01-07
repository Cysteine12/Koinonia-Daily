package org.eni.koinoniadaily.modules.bookmark;

import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkRequest;
import org.eni.koinoniadaily.modules.bookmarkcategory.BookmarkCategory;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

  public Bookmark toEntity(Long userId, BookmarkRequest request) {

    User user = User.builder().id(userId).build();

    Teaching teaching = Teaching.builder().id(request.getTeachingId()).build();

    BookmarkCategory category = BookmarkCategory.builder().id(request.getCategoryId()).build();

    return Bookmark.builder()
            .user(user)
            .category(category)
            .teaching(teaching)
            .note(request.getNote())
            .build();
  }
}