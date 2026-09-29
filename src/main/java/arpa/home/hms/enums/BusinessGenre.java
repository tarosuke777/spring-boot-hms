package arpa.home.hms.enums;

import java.util.Arrays;

/**
 * SE（システムエンジニア）の知識・スキルから派生する事業ジャンル定義
 */
public enum BusinessGenre {

  SYSTEM_DEV(1, "受託・開発支援", "bg-primary"), MOBILE_APP(2, "スマホアプリ", "bg-success"), TECH_MEDIA(3,
      "技術メディア・アフィリエイト", "bg-info text-dark"), DEV_TOOL(4, "開発・業務効率化ツール",
          "bg-warning text-dark"), SAAS_SERVICE(5, "SaaS・Webサービス", "bg-dark"), IT_CONSULTING(6,
              "ITコンサル・技術支援", "bg-secondary"), OTHER(99, "その他", "bg-light text-dark");

  private final int code;
  private final String label;
  private final String badgeClass;

  BusinessGenre(int code, String label, String badgeClass) {
    this.code = code;
    this.label = label;
    this.badgeClass = badgeClass;
  }

  public int getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public String getBadgeClass() {
    return badgeClass;
  }

  public static BusinessGenre fromCode(int code) {
    return Arrays.stream(values()).filter(genre -> genre.code == code).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid BusinessGenre code: " + code));
  }

  public static BusinessGenre safeValueOf(String name) {
    if (name == null) {
      return OTHER;
    }
    try {
      return valueOf(name);
    } catch (IllegalArgumentException e) {
      return OTHER;
    }
  }
}
