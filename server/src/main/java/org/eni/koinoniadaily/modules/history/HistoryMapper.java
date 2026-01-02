package org.eni.koinoniadaily.modules.history;

import org.eni.koinoniadaily.modules.history.dto.HistoryResponse;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapper {
  
  public HistoryResponse toDto(History history) {

    return HistoryResponse.builder()
            .id(history.getId())
            .teachingId(history.getTeaching().getId())
            .teachingTitle(history.getTeaching().getTitle())
            .teachingThumbnailUrl(history.getTeaching().getThumbnailUrl())
            .teachingTaughtAt(history.getTeaching().getTaughtAt())
            .isMarkedRead(history.isMarkedRead())
            .createdAt(history.getCreatedAt())
            .updatedAt(history.getUpdatedAt())
            .build();
  }

  public History toEntity(User user, Teaching teaching) {

    return History.builder()
            .teaching(teaching)
            .user(user)
            .build();
  }
}
