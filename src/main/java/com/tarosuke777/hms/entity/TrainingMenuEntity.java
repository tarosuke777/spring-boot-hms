package com.tarosuke777.hms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingMenuEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer trainingMenuId;

  private String trainingMenuName;
  private Integer targetAreaId;

  private String link;
  private Integer maxWeight;
  private Integer maxReps;
  private Integer maxSets;
}
