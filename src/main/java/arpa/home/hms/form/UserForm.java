package arpa.home.hms.form;

import arpa.home.hms.enums.Role;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.InsertGroup;
import arpa.home.hms.validation.OptionalPassword;
import arpa.home.hms.validation.UpdateGroup;
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
