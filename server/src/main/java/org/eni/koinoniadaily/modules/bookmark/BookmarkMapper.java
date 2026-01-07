package org.eni.koinoniadaily.modules.bookmark;

import org.eni.koinoniadaily.modules.bookmarkcategory.BookmarkCategory;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

  public Bookmark toEntity(User user, BookmarkCategory category, Teaching teaching, String note) {

    return Bookmark.builder()
            .user(user)
            .category(category)
            .teaching(teaching)
            .note(note)
            .build();
  }
}