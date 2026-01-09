package org.eni.koinoniadaily.modules.series;

import java.util.Optional;

import org.eni.koinoniadaily.modules.series.dto.SeriesPageResponse;
import org.eni.koinoniadaily.modules.series.dto.SeriesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

  @Query("SELECT new org.eni.koinoniadaily.modules.series.dto.SeriesPageResponse(" +
        "s.id, s.title, s.description, s.createdAt, s.updatedAt, SIZE(s.teachings)) " + 
        "FROM Series s")
  Page<SeriesPageResponse> findAllBy(Pageable pageable); 

  Optional<SeriesResponse> findProjectedById(Long id); 
}
