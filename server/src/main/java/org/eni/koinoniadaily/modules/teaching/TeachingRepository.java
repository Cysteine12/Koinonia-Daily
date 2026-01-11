package org.eni.koinoniadaily.modules.teaching;

import java.util.List;

import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingRepository extends JpaRepository<Teaching, Long> {  

  Page<TeachingWithoutMessageProjection> findAllBy(Pageable pageable);

  List<TeachingWithoutMessageProjection> findAllBySeriesId(Long seriesId);

  List<TeachingWithoutMessageProjection> findAllByCollections_Id(Long collectionId);
}
