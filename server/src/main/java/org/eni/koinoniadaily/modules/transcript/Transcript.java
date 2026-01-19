package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.entity.BaseEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "transcripts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Transcript extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String title;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(nullable = false, columnDefinition = "text")
  private String message;
}
