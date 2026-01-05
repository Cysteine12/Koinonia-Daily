package org.eni.koinoniadaily.modules.bookmarkcategory;

import java.util.Optional;

import org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkCategoryRepository extends JpaRepository<BookmarkCategory, Long> {

  @Query("SELECT new org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse(" +
          "bc.id, bc.name, SIZE(bc.bookmarks), bc.createdAt, bc.updatedAt) " +
          "FROM BookmarkCategory bc WHERE bc.user.id = :userId")
	Page<BookmarkCategoryResponse> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT new org.eni.koinoniadaily.modules.bookmarkcategory.dto.BookmarkCategoryResponse(" +
          "bc.id, bc.name, SIZE(bc.bookmarks), bc.createdAt, bc.updatedAt) " +
          "FROM BookmarkCategory bc WHERE bc.id = :id AND bc.user.id = :userId")
  Optional<BookmarkCategoryResponse> findByIdAndUserId(Long id, Long userId);

  Optional<BookmarkCategory> findByIdAndUser_Id(Long id, Long userId);
}
