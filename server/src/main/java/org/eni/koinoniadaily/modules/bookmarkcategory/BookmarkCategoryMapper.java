package org.eni.koinoniadaily.modules.bookmarkcategory;

import org.eni.koinoniadaily.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class BookmarkCategoryMapper {
  
  public BookmarkCategory toEntity(String name, User user) {

    return BookmarkCategory.builder()
            .name(name)
            .user(user)
            .build();
  }
}
