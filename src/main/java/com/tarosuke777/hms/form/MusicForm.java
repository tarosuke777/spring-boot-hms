package com.tarosuke777.hms.form;

import com.tarosuke777.hms.validation.DeleteGroup;
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
public class MusicForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer musicId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String musicName;

  private Integer artistId;

  private String link;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}

