package com.tarosuke777.hms.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

/**
 * Spring Securityの認証用ユーザー情報にIDを追加したカスタムクラス
 */
@Getter
public class LoginUser extends User {

    private final Integer id; // データベース上の主キーを保持

    public LoginUser(Integer id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}
