package org.eni.koinonia_daily.modules.history;

import java.time.LocalDateTime;

import org.eni.koinonia_daily.modules.teaching.Teaching;
import org.eni.koinonia_daily.modules.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "histories", 
  uniqueConstraints = @UniqueConstraint(
    name = "userId_teachingId", 
    columnNames = {"userId", "teachingId"}
  )
)
@Data @NoArgsConstructor @AllArgsConstructor
public class History {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teachingId", nullable = false)
  private Teaching teaching;

  @Column(nullable = false)
  private boolean isMarkedRead;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @PrePersist
  private void prePersist() {
    if (this.isMarkedRead) {
      this.isMarkedRead = false;
    }
  }
}
