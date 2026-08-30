package arpa.home.hms.form;

import arpa.home.hms.enums.PriorityLevel;
import arpa.home.hms.enums.TaskCategory;
import arpa.home.hms.enums.TaskStatus;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;

@Data
public class TaskForm implements Serializable {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotBlank
  @Size(max = 255)
  private String name;

  private String note;

  @NotNull(groups = UpdateGroup.class)
  private TaskStatus status;

  /**
   * タブ分類（今すぐやるタスク / 将来やりたいこと）
   */
  @NotNull(groups = UpdateGroup.class) // 新規・更新時に必要に応じて設定
  private TaskCategory category;

  /**
   * 重要度（高・低）※バックログ時は未入力（null）を許容
   */
  private PriorityLevel importance;

  /**
   * 緊急度（高・低）※バックログ時は未入力（null）を許容
   */
  private PriorityLevel urgency;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private TaskStatus searchStatus;

  /**
   * タスク一覧のカテゴリタブによる検索条件
   */
  private TaskCategory searchCategory;
}
