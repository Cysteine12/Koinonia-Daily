package org.eni.koinoniadaily.modules.history;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "histories", 
    uniqueConstraints = @UniqueConstraint(
        name = "unique_user_teaching", 
        columnNames = {"userId", "teachingId"}
    )
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class History extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teachingId", nullable = false)
  private Teaching teaching;

  @Column(nullable = false)
  @Builder.Default
  private boolean isMarkedRead = false;
}
