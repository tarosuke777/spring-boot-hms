package arpa.home.hms.enums;

public enum ActivityLogTargetType {
  BOOK("書籍"), VIDEO("動画"), GAME_VIDEO("ゲーム動画");

  private final String label;

  ActivityLogTargetType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static String normalizeLegacyValue(String value) {
    return "MOVIE".equals(value) ? VIDEO.name() : value;
  }
}
