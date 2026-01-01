package org.eni.koinoniadaily.modules.teaching;

import java.time.LocalDateTime;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.series.Series;
import org.eni.koinoniadaily.modules.transcript.Transcript;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachings")
@Getter 
@Setter 
@SuperBuilder
@NoArgsConstructor
public class Teaching extends BaseEntity {
  
  @Column(nullable = false, length = 50)
  private String title;
  
  @Column(nullable = false, length = 255)
  private String scripturalReferences;

  @Column(nullable = false, columnDefinition = "text")
  private String message;

  @Column(nullable = false, length = 255)
  private String summary;

  @Column(nullable = false, length = 100, unique = true)
  private String audioUrl;

  @Column(nullable = false, length = 100, unique = true)
  private String videoUrl;

  @Column(nullable = false, length = 100)
  private String thumbnailUrl;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TeachingType type;

  @Column(nullable = false)
  private String tags;
  
  @OneToOne
  @JoinColumn(name = "transcriptId", nullable = true)
  private Transcript transcript;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seriesId", nullable = true)
  private Series series;

  @Column(nullable = true)
  private int seriesPart;

  @Column(nullable = false)
  private LocalDateTime taughtAt;
}
