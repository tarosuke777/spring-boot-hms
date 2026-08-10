package com.tarosuke777.hms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCalendarForm {

  private Integer id;

  @NotBlank(message = "カレンダーIDは必須です")
  @Size(max = 255, message = "カレンダーIDは255文字以内で入力してください")
  private String calendarId;

  @Size(max = 50, message = "表示名は50文字以内で入力してください")
  private String name;

  @Pattern(regexp = "^$|^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "カラーコードの形式が不正です（例: #2952A3）")
  @Size(max = 7, message = "カラーコードは7文字以内で入力してください")
  private String color;

  private Integer version;
}
