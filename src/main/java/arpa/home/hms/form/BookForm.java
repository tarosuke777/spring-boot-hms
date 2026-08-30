package arpa.home.hms.form;

import arpa.home.hms.enums.BookGenre;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldNameConstants
public class BookForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotBlank
  @Size(min = 1, max = 50)
  private String name;

  @NotNull
  private Integer authorId;

  @URL
  @Size(max = 255)
  private String link;

  @NotNull(message = "ジャンルを選択してください")
  private BookGenre genre;

  private boolean isAdult;

  private String note;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}
