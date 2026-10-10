package arpa.home.hms.form;

import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldNameConstants
public class CodingLogForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotNull
  private LocalDate solvedDate;

  @NotBlank
  @URL
  @Size(max = 512)
  private String problemUrl;

  @Size(max = 2000)
  private String memo;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}
