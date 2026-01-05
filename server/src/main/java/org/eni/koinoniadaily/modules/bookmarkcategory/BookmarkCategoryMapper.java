package org.eni.koinoniadaily.modules.bookmarkcategory;

import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse;
import org.eni.koinoniadaily.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class BookmarkCategoryMapper {

  public BookmarkCategoryResponse toDto(BookmarkCategory category) {

    return BookmarkCategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
  }
  
  public BookmarkCategory toEntity(String name, User user) {

    return BookmarkCategory.builder()
            .name(name)
            .user(user)
            .build();
  }
}
