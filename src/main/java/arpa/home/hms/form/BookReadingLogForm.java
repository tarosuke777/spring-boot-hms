package arpa.home.hms.form;

import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotNull;
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
public class BookReadingLogForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotNull
  private Integer bookId;

  @NotNull
  private LocalDate readDate;

  @Size(max = 2000)
  private String memo;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}
