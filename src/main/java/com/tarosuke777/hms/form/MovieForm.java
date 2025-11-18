package com.tarosuke777.hms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieForm {

  private Integer movieId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String movieName;

  private String note;
}
