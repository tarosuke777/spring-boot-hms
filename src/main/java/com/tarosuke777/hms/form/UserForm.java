package com.tarosuke777.hms.form;

import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer userId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String userName;

  @NotBlank
  @Size(min = 4, max = 20)
  @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Enter {0} in single-byte alphanumeric characters")
  private String password;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;

}
