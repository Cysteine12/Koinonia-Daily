package org.eni.koinoniadaily.modules.teaching;

import java.util.List;

import aj.org.objectweb.asm.commons.Remapper;
import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TeachingRepository extends JpaRepository<Teaching, Long> {  

  Page<TeachingWithoutMessageProjection> findAllBy(Pageable pageable);

  Page<TeachingWithoutMessageProjection> findAllByStatus(TeachingStatus status, Pageable pageable);

  List<TeachingWithoutMessageProjection> findAllBySeriesId(Long seriesId);

  List<TeachingWithoutMessageProjection> findAllByCollectionsId(Long collectionId);

  Page<TeachingWithoutMessageProjection> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  boolean existsByTranscriptId(Long id);

  @Modifying
  @Transactional
  @Query("UPDATE Teaching t SET t.status = 'EMBEDDING' WHERE t.id = :id AND t.status != 'EMBEDDING'")
  int markAsEmbedding(@Param("id") Long id);

  @Modifying
  @Transactional
  @Query("UPDATE Teaching t SET t.status = :status WHERE t.id = :id")
  void updateStatus(@Param("id") Long id, @Param("status") TeachingStatus status);
}
