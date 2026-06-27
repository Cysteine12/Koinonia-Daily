package org.eni.koinoniadaily.modules.search;

import java.util.Optional;

import org.eni.koinoniadaily.modules.search.projection.RecentSearchClick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
        """,
	countQuery = """
	SELECT COUNT(*)
	FROM search_clicks sc
	WHERE sc.user_id = :userId
	""",
	nativeQuery = true)
    Page<RecentSearchClick> findAllByUserId(Long userId, Pageable pageable);

    Optional<SearchClick> findByTeachingIdAndUserId(Long teachingId, Long userId);
}
