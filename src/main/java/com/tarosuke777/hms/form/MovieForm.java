package com.tarosuke777.hms.form;

import com.tarosuke777.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieForm {

  @NotNull(groups = UpdateGroup.class)
  private Integer movieId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String movieName;

  private String note;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}
