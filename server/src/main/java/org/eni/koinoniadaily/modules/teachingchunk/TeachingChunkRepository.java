package org.eni.koinoniadaily.modules.teachingchunk;

import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TeachingChunkRepository extends JpaRepository<TeachingChunk,Long> {

  @Transactional
  @Query(value = """
      UPDATE teaching_chunks
      SET embedding_status = 'PROCESSING',
        updated_at = NOW()
      WHERE id IN (
          SELECT id
          FROM teaching_chunks
          WHERE teaching_id = :teachingId
            AND (
                embedding_status = 'PENDING'
                OR (embedding_status = 'PROCESSING' AND updated_at < NOW() - INTERVAL '1 hour')
            )
          ORDER BY chunk_index
          LIMIT :limit
          FOR UPDATE SKIP LOCKED
      )
      RETURNING *;
      """, nativeQuery = true)
  List<TeachingChunk> claimNextPendingChunks(
      @Param("teachingId") Long teachingId,
      @Param("limit") int limit
  );

  boolean existsByTeachingIdAndEmbeddingStatusNot(Long teachingId, EmbeddingStatus embeddingStatus);

  @Modifying
  @Transactional
  @Query("UPDATE TeachingChunk t SET t.embeddingStatus = 'PENDING' " +
      "WHERE t.teaching.id = :teachingId AND t.embeddingStatus = 'FAILED'")
  void resetFailedChunks(@Param("teachingId") Long teachingId);

  @Modifying
  @Transactional
  @Query("DELETE FROM TeachingChunk tc WHERE tc.teaching.id = :teachingId")
  void deleteByTeachingId(@Param("teachingId") Long teachingId);
}
