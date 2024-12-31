package com.tarosuke777.hms.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MusicForm {
  private Integer musicId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String musicName;

  private Integer artistId;
}
