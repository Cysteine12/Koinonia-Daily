package org.eni.koinoniadaily.modules.teaching;

import java.util.List;

import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingRepository extends JpaRepository<Teaching, Long> {  

  Page<TeachingPageResponse> findAllBy(Pageable pageable);

  List<TeachingPageResponse> findAllBySeriesId(Long seriesId);
}
