package com.tarosuke777.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class TrainingMenuEntity {

  private Integer trainingMenuId;
  private String trainingMenuName;
  private Integer targetAreaId;
  private String link;
}
