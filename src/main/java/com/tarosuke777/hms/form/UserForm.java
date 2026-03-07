package com.tarosuke777.hms.form;

import com.tarosuke777.hms.enums.Role;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.InsertGroup;
import com.tarosuke777.hms.validation.OptionalPassword;
import com.tarosuke777.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotBlank
  @Size(min = 1, max = 50)
  private String name;

  @NotBlank(groups = InsertGroup.class)
  @OptionalPassword
  private String password;

  /** ユーザーのロール（更新時のみ使用） */
  @NotNull(groups = UpdateGroup.class)
  private Role role;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;

}
