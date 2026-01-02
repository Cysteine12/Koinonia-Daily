package org.eni.koinoniadaily.modules.history;

import java.util.Optional;

import org.eni.koinoniadaily.modules.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

  boolean existsByIdAndUserId(Long id, Long userId);
  
  Optional<History> findByUserIdAndTeachingId(Long userId, Long teachingId);

  Page<History> findAllByUser(User user, Pageable pageable);

  Optional<History> findByIdAndUserId(Long id, Long userId);
  
  void deleteByIdAndUserId(Long id, Long userId);
}
