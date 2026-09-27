package arpa.home.hms.enums;

public enum BusinessStatus {
  IDEA(1, "アイデア"), VALIDATION(2, "検証中"), OPERATING(3, "運用中"), WITHDRAWN(4, "撤退");

  private final int code;
  private final String label;

  BusinessStatus(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public int getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public static BusinessStatus fromValue(int code) {
    for (BusinessStatus status : values()) {
      if (status.code == code) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid BusinessStatus code: " + code);
  }
}
