package com.tarosuke777.hms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainingMenuForm {

  private Integer trainingMenuId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String trainingMenuName;
  
  private Integer targetAreaId;
  
  private String link;
}
