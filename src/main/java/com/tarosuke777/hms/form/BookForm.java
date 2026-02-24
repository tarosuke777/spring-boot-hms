package com.tarosuke777.hms.form;

import org.hibernate.validator.constraints.URL;
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
public class BookForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer bookId;

  @NotBlank
  @Size(min = 1, max = 50)
  private String bookName;

  @NotNull
  private Integer authorId;

  @URL
  @Size(min = 1, max = 255)
  private String link;

  private String note;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;

}
