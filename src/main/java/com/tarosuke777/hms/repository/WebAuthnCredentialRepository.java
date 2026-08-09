package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.WebAuthnCredentialEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebAuthnCredentialRepository
    extends JpaRepository<WebAuthnCredentialEntity, Long> {

  // クレデンシャルIDで鍵を検索
  Optional<WebAuthnCredentialEntity> findByCredentialId(String credentialId);

  // ユーザーに紐づくすべてのパスキーを取得
  List<WebAuthnCredentialEntity> findByUserId(Integer userId);

  void deleteByCredentialId(String credentialId);
}
