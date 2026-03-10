package com.tarosuke777.hms.enums;

public enum BookGenre {

    // @formatter:off
    TECHNICAL(1, "技術書/仕事"),
    MANGA(2, "漫画"),
    NOVEL(3, "小説/ラノベ"),
    OTHER(99, "その他");
    // @formatter:on

    private final int code;
    private final String label;

    BookGenre(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
