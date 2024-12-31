package com.tarosuke777.hms.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArtistForm {

  private Integer artistId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String artistName;
}
