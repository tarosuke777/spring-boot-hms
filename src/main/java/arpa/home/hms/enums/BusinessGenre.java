package arpa.home.hms.enums;

public enum BusinessGenre {
  SAAS(1, "SaaS"), ECOMMERCE(2, "EC"), FINANCE(3, "金融"), EDUCATION(4, "教育"), HEALTHCARE(5,
      "医療"), OTHER(99, "その他");

  private final int code;
  private final String label;

  BusinessGenre(int code, String label) {
    this.code = code;
    this.label = label;
  }

  public int getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public static BusinessGenre fromValue(int code) {
    for (BusinessGenre genre : values()) {
      if (genre.code == code) {
        return genre;
      }
    }
    throw new IllegalArgumentException("Invalid BusinessGenre code: " + code);
  }
}
