package com.tarosuke777.hms.enums;

public enum MovieGenre {

    // @formatter:off
    REAL(1, "実写"),
    ANIME(2, "アニメ"),
    OTHER(99, "その他");
    // @formatter:on

    private final int code;
    private final String label;

    MovieGenre(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static MovieGenre fromValue(int value) {
        for (MovieGenre genre : values()) {
            if (genre.code == value) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Invalid genre value: " + value);
    }
}
