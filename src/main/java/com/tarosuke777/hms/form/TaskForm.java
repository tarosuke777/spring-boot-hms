package com.tarosuke777.hms.form;

import com.tarosuke777.hms.enums.PriorityLevel;
import com.tarosuke777.hms.enums.TaskCategory;
import com.tarosuke777.hms.enums.TaskStatus;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
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
