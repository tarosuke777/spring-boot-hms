package com.tarosuke777.hms.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        // 1. セキュリティコンテキスト（ログイン情報が詰まった箱）を取得
        return Optional.ofNullable(SecurityContextHolder.getContext())
                // 2. 認証オブジェクトを取得
                .map(SecurityContext::getAuthentication)
                // 3. 認証済みか、ログイン中かを確認
                .filter(Authentication::isAuthenticated)
                // 4. Principal（ログインユーザー本体）を取得
                .map(Authentication::getPrincipal)
                // 5. ログイン中のユーザー名を取り出す
                .map(principal -> {
                    if (principal instanceof UserDetails user) {
                        return user.getUsername();
                    }
                    // 「ログインしていない」または「匿名ユーザー」の状態
                    return null;
                });
    }

}
