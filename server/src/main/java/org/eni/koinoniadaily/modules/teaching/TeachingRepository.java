package org.eni.koinoniadaily.modules.teaching;

import java.util.List;

import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingRepository extends JpaRepository<Teaching, Long> {  

  Page<TeachingWithoutMessageProjection> findAllBy(Pageable pageable);

  List<TeachingWithoutMessageProjection> findAllBySeriesId(Long seriesId);

  List<TeachingWithoutMessageProjection> findAllByCollectionsId(Long collectionId);

  @Query("SELECT t FROM Teaching t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
  Page<TeachingWithoutMessageProjection> searchByTitle(@Param("title") String title, Pageable pageable);
}
