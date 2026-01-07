package org.eni.koinoniadaily.modules.bookmark;

import java.util.Optional;

import org.eni.koinoniadaily.modules.bookmark.dto.BookmarkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  @Query("SELECT new org.eni.koinoniadaily.modules.bookmark.dto.BookmarkResponse(" + 
        "b.id, t.id, t.title, t.thumbnailUrl, t.taughtAt, b.note, b.createdAt, b.updatedAt) " + 
        "FROM Bookmark b JOIN b.teaching t WHERE b.category.id = :categoryId AND b.user.id = :userId")
  Page<BookmarkResponse> findAllByCategoryIdAndUserId(@Param("categoryId") Long categoryId, @Param("userId") Long userId, Pageable pageable);  

  Optional<Bookmark> findByCategoryIdAndTeachingId(Long categoryId, Long teachingId);

  Optional<Bookmark> findByIdAndUserId(Long id, Long userId);

  boolean existsByIdAndUserId(Long id, Long userId);
}
