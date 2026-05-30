package org.eni.koinoniadaily.modules.teachingchunk;

import org.eni.koinoniadaily.modules.search.projection.SearchResult;
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

  @Modifying
  @Transactional
  @Query("UPDATE TeachingChunk t SET t.embeddingStatus = 'PENDING' " +
      "WHERE t.teaching.id = :teachingId AND t.embeddingStatus = 'FAILED'")
  void resetFailedChunks(@Param("teachingId") Long teachingId);

  @Modifying
  @Transactional
  @Query("DELETE FROM TeachingChunk tc WHERE tc.teaching.id = :teachingId")
  void deleteByTeachingId(@Param("teachingId") Long teachingId);

  @Query(value = """
      WITH
      
      semantic_candidates AS (
          SELECT
              chunk_id,
              teaching_id,
              ROW_NUMBER() OVER (ORDER BY distance) AS semantic_rank,
              0 AS lexical_rank,
              0 AS title_rank,
              'SEMANTIC' AS source
      
          FROM (
              SELECT
                  tc.id    AS chunk_id,
                  tc.teaching_id,
                  ce.embedding <=> CAST(:query_embedding AS vector) AS distance
              FROM chunk_embeddings ce
      
              JOIN teaching_chunks tc ON tc.id = ce.teaching_chunk_id
              WHERE ce.model = :model
      
              ORDER BY distance
              LIMIT 80
          ) sem_inner
      ),
      
      parsed_tsquery AS (
          SELECT websearch_to_tsquery('english', :query) AS q
      ),
      
      lexical_candidates AS (
          SELECT
              chunk_id,
              teaching_id,
              0 AS semantic_rank,
              ROW_NUMBER() OVER (ORDER BY lex_score DESC) AS lexical_rank,
              0 AS title_rank,
              'FTS' AS source
      
          FROM (
              SELECT
                  tc.id AS chunk_id,
                  tc.teaching_id,
                  ts_rank(tc.tsv, q.q) AS lex_score
              FROM teaching_chunks tc
              CROSS JOIN parsed_tsquery q
      
              WHERE tc.tsv @@ q.q
      
              ORDER BY lex_score DESC
              LIMIT 50
          ) lex_inner
      ),
      
      title_candidates AS (
          SELECT
              chunk_id,
              teaching_id,
              0 AS semantic_rank,
              0 AS lexical_rank,
              ROW_NUMBER() OVER (ORDER BY title_sim DESC) AS title_rank,
              'TITLE' AS source
      
          FROM (
              SELECT
                  tc.id AS chunk_id,
                  tc.teaching_id,
                  similarity(LOWER(t.title), LOWER(:query)) AS title_sim
              FROM teachings t
              JOIN teaching_chunks tc
                ON tc.teaching_id = t.id
                AND tc.chunk_index = 0
      
              WHERE similarity(LOWER(t.title), LOWER(:query)) > 0.3
      
              ORDER BY title_sim DESC, t.taught_at DESC
              LIMIT 20
          ) title_inner
      ),
      
      all_candidates AS (
      
          SELECT * FROM semantic_candidates
      
          UNION ALL
      
          SELECT * FROM lexical_candidates
      
          UNION ALL
      
          SELECT * FROM title_candidates
      ),
      
      aggregated AS (
      
          SELECT
              chunk_id,
              teaching_id,
      
              MIN(NULLIF(semantic_rank, 0)) AS best_semantic_rank,
              MIN(NULLIF(lexical_rank, 0)) AS best_lexical_rank,
              MIN(NULLIF(title_rank, 0)) AS best_title_rank,
      
              STRING_AGG(DISTINCT source, ',' ORDER BY source) AS match_sources
      
          FROM all_candidates
      
          GROUP BY chunk_id, teaching_id
      )
      
      SELECT
          t.id              AS teaching_id,
          tc.id             AS chunk_id,
          t.title           AS teaching_title,
          tc.section_title,
          tc.content,
          tc.chunk_index,
          t.thumbnail_url,
          t.type,
          t.taught_at,
      
          (
              (1.2 * COALESCE(1.0 / (60 + aggregated.best_semantic_rank), 0))
            + (1.0 * COALESCE(1.0 / (60 + aggregated.best_lexical_rank), 0))
            + (1.4 * COALESCE(1.0 / (60 + aggregated.best_title_rank), 0))
          ) AS score,
      
          aggregated.match_sources
      
      FROM aggregated
      
      JOIN teaching_chunks tc ON tc.id = aggregated.chunk_id
      
      JOIN teachings t ON t.id = aggregated.teaching_id
      
      ORDER BY score DESC
      OFFSET :offset
      LIMIT :limit
      """, nativeQuery = true)
  List<SearchResult> search(
      @Param("query") String query,
      @Param("query_embedding") float[] queryEmbedding,
      @Param("model") String model,
      @Param("offset") int offset,
      @Param("limit") int limit
  );

  @Query(value = """
      WITH
      
      parsed_tsquery AS (
          SELECT websearch_to_tsquery('english', :query) AS q
      ),
      
      lexical_candidates AS (
          SELECT
              chunk_id,
              teaching_id,
              ROW_NUMBER() OVER (ORDER BY lex_score DESC) AS lexical_rank,
              0 AS title_rank,
              'FTS' AS source
      
          FROM (
              SELECT
                  tc.id AS chunk_id,
                  tc.teaching_id,
                  ts_rank(tc.tsv, q.q) AS lex_score
              FROM teaching_chunks tc
              CROSS JOIN parsed_tsquery q
      
              WHERE tc.tsv @@ q.q
      
              ORDER BY lex_score DESC
              LIMIT 50
          ) lex_inner
      ),
      
      title_candidates AS (
          SELECT
              chunk_id,
              teaching_id,
              0 AS lexical_rank,
              ROW_NUMBER() OVER (ORDER BY title_sim DESC) AS title_rank,
              'TITLE' AS source
      
          FROM (
              SELECT
                  tc.id AS chunk_id,
                  tc.teaching_id,
                  similarity(LOWER(t.title), LOWER(:query)) AS title_sim
              FROM teachings t
              JOIN teaching_chunks tc
                ON tc.teaching_id = t.id
                AND tc.chunk_index = 0
      
              WHERE similarity(LOWER(t.title), LOWER(:query)) > 0.3
      
              ORDER BY title_sim DESC, t.taught_at DESC
              LIMIT 30
          ) title_inner
      ),
      
      all_candidates AS (
      
          SELECT * FROM lexical_candidates
      
          UNION ALL
      
          SELECT * FROM title_candidates
      ),
      
      aggregated AS (
      
          SELECT
              chunk_id,
              teaching_id,
      
              MIN(NULLIF(lexical_rank, 0)) AS best_lexical_rank,
              MIN(NULLIF(title_rank, 0)) AS best_title_rank,
      
              STRING_AGG(DISTINCT source, ',' ORDER BY source) AS match_sources
      
          FROM all_candidates
      
          GROUP BY chunk_id, teaching_id
      )
      
      SELECT
          t.id              AS teaching_id,
          tc.id             AS chunk_id,
          t.title           AS teaching_title,
          tc.section_title,
          tc.content,
          tc.chunk_index,
          t.thumbnail_url,
          t.type,
          t.taught_at,
      
          (
            + (1.0 * COALESCE(1.0 / (60 + aggregated.best_lexical_rank), 0))
            + (1.4 * COALESCE(1.0 / (60 + aggregated.best_title_rank), 0))
          ) AS score,
      
          aggregated.match_sources
      
      FROM aggregated
      
      JOIN teaching_chunks tc ON tc.id = aggregated.chunk_id
      
      JOIN teachings t ON t.id = aggregated.teaching_id
      
      ORDER BY score DESC
      OFFSET :offset
      LIMIT :limit
      """, nativeQuery = true)
  List<SearchResult> search(
      @Param("query") String query,
      @Param("offset") int offset,
      @Param("limit") int limit
  );
}
