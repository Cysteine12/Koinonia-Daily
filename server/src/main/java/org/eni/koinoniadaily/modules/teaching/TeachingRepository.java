package org.eni.koinoniadaily.modules.teaching;

import java.util.List;

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

  Page<TeachingWithoutMessageProjection> findAllByEmbeddingStatus(EmbeddingStatus status, Pageable pageable);

  List<TeachingWithoutMessageProjection> findAllBySeriesId(Long seriesId);

  List<TeachingWithoutMessageProjection> findAllByCollectionsId(Long collectionId);

  Page<TeachingWithoutMessageProjection> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  boolean existsByTranscriptId(Long id);

  @Modifying
  @Transactional
  @Query("UPDATE Teaching t SET t.embeddingStatus = 'EMBEDDING' WHERE t.id = :id AND t.embeddingStatus != 'EMBEDDING'")
  int markAsEmbedding(@Param("id") Long id);

  @Modifying
  @Transactional
  @Query("UPDATE Teaching t SET t.embeddingStatus = :status WHERE t.id = :id")
  void updateEmbeddingStatus(@Param("id") Long id, @Param("status") EmbeddingStatus status);

  @Modifying
  @Query(value = """
      UPDATE teachings t
      SET embedding_status =
        CASE
          WHEN EXISTS (
            SELECT 1
            FROM teaching_chunks tc
            WHERE tc.teaching_id = t.id
            AND tc.embedding_status != 'EMBEDDED'
          )
          THEN 'FAILED'
          ELSE 'EMBEDDED'
        END
      WHERE t.id = :id
      """, nativeQuery = true)
  void finalizeEmbeddingStatus(@Param("id") Long id);
}
