package org.eni.koinoniadaily.modules.bookmark;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.bookmarkcategory.BookmarkCategory;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.user.User;

import jakarta.persistence.Column;
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
    name = "bookmarks",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_user_teaching_category",
        columnNames = {"userId", "teachingId", "categoryId"}
    )
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Bookmark extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teachingId", nullable = false)
  private Teaching teaching;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoryId", nullable = false)
  private BookmarkCategory category;

  @Column(length = 500)
  private String note;
}
