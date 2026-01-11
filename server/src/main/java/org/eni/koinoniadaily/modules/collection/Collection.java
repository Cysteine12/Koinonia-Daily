package org.eni.koinoniadaily.modules.collection;

import java.util.ArrayList;
import java.util.List;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.teaching.Teaching;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "collections")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Collection extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String name;  

  @Column(nullable = false, length = 255)
  private String description; 

  @Column(nullable = false, length = 100)
  private String thumbnailUrl;  

  @ManyToMany
  @JoinTable(
      name = "collection_teachings",
      joinColumns = @JoinColumn(name = "collectionId"),
      inverseJoinColumns = @JoinColumn(name = "teachingId")
  )
  @Builder.Default
  private List<Teaching> teachings = new ArrayList<>();

  public void addTeaching(Teaching teaching) {
    teachings.add(teaching);
    teaching.getCollections().add(this);
  }

  public void removeTeaching(Teaching teaching) {
    teachings.remove(teaching);
    teaching.getCollections().remove(this);
  }
}
