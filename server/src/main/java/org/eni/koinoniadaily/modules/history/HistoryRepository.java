package org.eni.koinoniadaily.modules.history;

import java.util.Optional;

import org.eni.koinoniadaily.modules.history.dto.HistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

  boolean existsByIdAndUserId(Long id, Long userId);
  
  Optional<History> findByUserIdAndTeachingId(Long userId, Long teachingId);

  @Query("SELECT new org.eni.koinoniadaily.modules.history.dto.HistoryResponse(" + 
          "h.id, t.id, t.title, t.thumbnailUrl, t.taughtAt, h.isMarkedRead, h.createdAt, h.updatedAt) " + 
          "FROM History h JOIN h.teaching t WHERE h.user.id = :userId")
  Page<HistoryResponse> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  Optional<History> findByIdAndUserId(Long id, Long userId);
  
  void deleteByIdAndUserId(Long id, Long userId);
}
