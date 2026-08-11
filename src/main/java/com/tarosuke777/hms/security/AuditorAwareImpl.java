package com.tarosuke777.hms.security;

import com.tarosuke777.hms.service.UserDetailServiceImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Integer> {

  private final UserDetailServiceImpl userDetailsService;

  @Override
  @NonNull
  public Optional<Integer> getCurrentAuditor() {
    // セキュリティコンテキスト（ログイン情報が詰まった箱）を取得
    return Optional.ofNullable(SecurityContextHolder.getContext())
        // 認証オブジェクトを取得
        .map(SecurityContext::getAuthentication)
        // 認証済みか、ログイン中かを確認
        .filter(Authentication::isAuthenticated)
        // Principal（ログインユーザー本体）を取得
        .map(Authentication::getPrincipal)
        // ログイン中のユーザー名を取り出す
        .map(principal -> {

          // 通常フォーム認証等の場合
          if (principal instanceof LoginUser user) {
            return user.getId();
          }

          // WebAuthn認証の場合
          if (principal instanceof PublicKeyCredentialUserEntity userEntity) {
            var loadedUser = userDetailsService.loadUserByUsername(userEntity.getName());
            if (loadedUser instanceof LoginUser user) {
              return user.getId();
            }
          }

          // 「ログインしていない」または「匿名ユーザー」の状態
          return null;
        });
  }
}
