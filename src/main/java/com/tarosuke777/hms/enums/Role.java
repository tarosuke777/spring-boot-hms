package com.tarosuke777.hms.enums;

public enum Role {
    ADMIN(1), GENERAL(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        for (Role role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }

    /**
     * Spring Securityの認可処理で使用する権限文字列を返します。 <br>
     * Spring Securityの仕様により、ROLE_プレフィックスを付与して返します。
     *
     * @return "ROLE_" + ロール名
     */
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
