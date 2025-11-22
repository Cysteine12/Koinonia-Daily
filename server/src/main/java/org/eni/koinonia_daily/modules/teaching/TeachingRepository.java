package org.eni.koinonia_daily.modules.teaching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingRepository extends JpaRepository<Teaching, Long> {  
}
