package org.eni.koinonia_daily.modules.series;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eni.koinonia_daily.modules.teaching.Teaching;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "series")
@Data @NoArgsConstructor @AllArgsConstructor
public class Series {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable= false)
  private String title;

  @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Teaching> teachings = new ArrayList<>();

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
