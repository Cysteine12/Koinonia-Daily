package org.eni.koinoniadaily.modules.transcript;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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
  @Builder.Default
  @Column(nullable = false, columnDefinition = "text")
  private String message = "";

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TranscriptStatus status = TranscriptStatus.PENDING;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> metadata;
}
