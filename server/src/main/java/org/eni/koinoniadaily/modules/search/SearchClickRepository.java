package org.eni.koinoniadaily.modules.search;

import java.time.Instant;
import java.util.Optional;

import org.eni.koinoniadaily.modules.search.projection.RecentSearchClick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SearchClickRepository extends JpaRepository<SearchClick, Long> {

    @Query(value = """
        SELECT
	    sc.id,
	    t.id AS teachingId,
	    t.title,
	    t.thumbnail_url AS thumbnailUrl,
	    t.type,
	    t.taught_at AS taughtAt
	FROM search_clicks sc
	JOIN teachings t ON sc.teaching_id = t.id	
	WHERE sc.user_id = :userId
	ORDER BY sc.updated_at DESC
        """,
	countQuery = """
	SELECT COUNT(*)
	FROM search_clicks sc
	WHERE sc.user_id = :userId
	""",
	nativeQuery = true)
    Page<RecentSearchClick> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = """
	INSERT INTO search_clicks (teaching_id, user_id)
	VALUES (:teachingId, :userId)
	ON CONFLICT (teaching_id, user_id)
	DO UPDATE
	SET updated_at = :updatedAt
	""", nativeQuery = true)
    void upsertByTeachingIdAndUserId(@Param("teachingId") Long teachingId, @Param("userId") Long userId, @Param("updatedAt") Instant updatedAt);
}
