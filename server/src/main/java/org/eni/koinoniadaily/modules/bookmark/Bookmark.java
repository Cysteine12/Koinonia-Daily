package org.eni.koinoniadaily.modules.bookmark;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "bookmarks",
    uniqueConstraints = @UniqueConstraint(
        name = "category_userId_teachingId",
        columnNames = {"category", "userId", "teachingId"}
    )
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Bookmark extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teachingId", nullable = false)
  private Teaching teaching;

  @PrePersist
  void prePersist() {
    if (this.category == null) { 
      this.category = "general"; 
    }
  }
}
