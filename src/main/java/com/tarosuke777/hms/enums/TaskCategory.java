package com.tarosuke777.hms.enums;

import java.util.Arrays;

public enum TaskCategory {
  IMMEDIATE(1, "今すぐやるタスク"), BACKLOG(2, "将来やりたいこと");

  private final int code;
  private final String label;

  TaskCategory(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public int getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public static TaskCategory fromValue(int code) {
    return Arrays.stream(values()).filter(category -> category.code == code).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid TaskCategory code: " + code));
  }
}
