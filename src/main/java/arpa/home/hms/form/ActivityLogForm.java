package arpa.home.hms.form;

import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldNameConstants
public class ActivityLogForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotNull
  private LocalDate activityDate;

  @NotBlank
  @Size(max = 32)
  @Pattern(regexp = "BOOK|VIDEO|GAME_VIDEO")
  private String targetType;

  @NotNull
  private Integer targetId;

  @Size(max = 2000)
  private String memo;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}
