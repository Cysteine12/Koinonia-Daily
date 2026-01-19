package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.modules.transcript.projection.TranscriptWithoutMessageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {

  Page<TranscriptWithoutMessageProjection> findAllBy(Pageable pageable);

  Page<TranscriptWithoutMessageProjection> findByTitleContainingIgnoreCase(String query, Pageable pageable);  
}
