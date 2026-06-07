package org.eni.koinoniadaily.modules.teachingembedding;

import org.eni.koinoniadaily.modules.recommendation.projection.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeachingEmbeddingRepository extends JpaRepository<TeachingEmbedding, Long> {

  Optional<TeachingEmbedding> findByTeachingId(Long teachingId);

  @Query(value = """
      SELECT
        t.id,
        t.title,
        t.summary,
        t.thumbnail_url,
        t.type,
        t.series_part,
        t.taught_at,
        t.created_at,
        t.updated_at,
        te.embedding <=> CAST(:teaching_embedding AS vector) AS score
      FROM teaching_embeddings te
      
      JOIN teachings t ON t.id = :te.teaching_id
      WHERE te.teaching_id != :teaching_id
      AND model = :model
      
      ORDER BY score
      LIMIT :size
      """, nativeQuery = true)
  List<RecommendationResult> getSimilarTeachings(
      @Param("teaching_embedding") float[] teachingEmbedding,
      @Param("model") String model,
      @Param("teaching_id") Long teachingId,
      @Param("size") int size
  );
}
