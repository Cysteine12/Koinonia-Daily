package org.eni.koinoniadaily.modules.search;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "search_clicks", 
    uniqueConstraints = @UniqueConstraint(
        name = "search_clicks_teaching_user_unique",
        columnNames = {"userId", "teachingId"}
    )
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SearchClick extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teaching_id", nullable = false)
  private Teaching teaching;
}
