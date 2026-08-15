package com.tarosuke777.hms.enums;

import java.util.Arrays;

public enum PriorityLevel {
  HIGH(1, "高"), LOW(2, "低");

  private final int code;
  private final String label;

  PriorityLevel(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public int getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public static PriorityLevel fromValue(int code) {
    return Arrays.stream(values()).filter(priority -> priority.code == code).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid PriorityLevel code: " + code));
  }
}
