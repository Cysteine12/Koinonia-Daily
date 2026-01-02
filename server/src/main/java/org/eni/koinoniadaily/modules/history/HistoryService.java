package org.eni.koinoniadaily.modules.history;

import java.util.Optional;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.auth.CurrentUserProvider;
import org.eni.koinoniadaily.modules.history.dto.HistoryRequest;
import org.eni.koinoniadaily.modules.history.dto.HistoryResponse;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRepository;
import org.eni.koinoniadaily.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {
  
  private final HistoryRepository historyRepository;
  private final HistoryMapper historyMapper;
  private final UserRepository userRepository;
  private final TeachingRepository teachingRepository;
  private final CurrentUserProvider currentUserProvider;
  private final static String UPDATED_AT = "updatedAt";

  public PageResponse<HistoryResponse> getHistoriesByUser(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Long userId = currentUserProvider.getCurrentUserId();

    User user = userRepository.getReferenceById(userId);

    Page<HistoryResponse> histories = historyRepository.findAllByUser(user, pageable)
                                        .map(historyMapper::toDto);

    return PageResponse.from(histories);
  }

  @Transactional
  public History createOrUpdateHistory(Long teachingId) {

    Long userId = currentUserProvider.getCurrentUserId();

    Optional<History> existingHistory = historyRepository.findByUserIdAndTeachingId(userId, teachingId);

    if (existingHistory.isPresent()) {
      existingHistory.get().setUpdatedAt(null);

      return existingHistory.get();
    }

    User user = userRepository.getReferenceById(userId);

    Teaching teaching = teachingRepository.getReferenceById(teachingId);

    return historyRepository.save(historyMapper.toEntity(user, teaching));
  }

  @Transactional
  public HistoryResponse updateHistoryMarkAsRead(Long id, HistoryRequest payload) {

    Long userId = currentUserProvider.getCurrentUserId();

    History history = historyRepository.findByIdAndUserId(id, userId)
                        .orElseThrow(() -> new NotFoundException("History not found"));
              
    history.setMarkedRead(payload.getIsMarkedRead());

    return historyMapper.toDto(history);
  }

  @Transactional
  public void deleteHistory(Long id) {

    Long userId = currentUserProvider.getCurrentUserId();

    if (!historyRepository.existsByIdAndUserId(id, userId)) {
      throw new NotFoundException("History not found");
    }

    historyRepository.deleteByIdAndUserId(id, userId);
  }
}
