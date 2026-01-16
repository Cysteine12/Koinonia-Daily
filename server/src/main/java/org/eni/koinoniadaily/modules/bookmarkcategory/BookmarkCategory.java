package org.eni.koinoniadaily.modules.bookmarkcategory;

import java.util.ArrayList;
import java.util.List;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.bookmark.Bookmark;
import org.eni.koinoniadaily.modules.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "bookmark_categories")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BookmarkCategory extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Bookmark> bookmarks = new ArrayList<>();
}
