package com.tarosuke777.hms.enums;

import java.util.Arrays;

public enum TaskStatus {
  TODO(1, "未処理"), COMPLETED(2, "完了"), CANCELLED(3, "キャンセル");

  private final int code;
  private final String label;

  TaskStatus(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public int getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public static TaskStatus fromValue(int code) {
    return Arrays.stream(values()).filter(status -> status.code == code).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid TaskStatus code: " + code));
  }

}
