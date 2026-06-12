package org.eni.koinoniadaily.modules.transcript;

import jakarta.persistence.*;
import lombok.Builder;
import org.eni.koinoniadaily.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "transcripts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Transcript extends BaseEntity {

  @Column(nullable = false, length = 60)
  private String title;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(nullable = false, columnDefinition = "text")
  private String message;

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TranscriptStatus status = TranscriptStatus.PENDING;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> metadata;
}
