package com.tarosuke777.hms.service;

import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.entity.WebAuthnCredentialEntity;
import com.tarosuke777.hms.repository.UserRepository;
import com.tarosuke777.hms.repository.WebAuthnCredentialRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutableCredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class WebAuthnService
    implements PublicKeyCredentialUserEntityRepository, UserCredentialRepository {

  private final UserRepository userRepository;
  private final WebAuthnCredentialRepository credentialRepository;

  public WebAuthnService(UserRepository userRepository,
      WebAuthnCredentialRepository credentialRepository) {
    this.userRepository = userRepository;
    this.credentialRepository = credentialRepository;
  }

  // --- UserCredentialRepository の実装 ---

  @Override
  public CredentialRecord findByCredentialId(Bytes credentialId) {

    String searchKey = credentialId.toBase64UrlString();
    // log.info("[DEBUG] Searching WebAuthn Credential by credentialId (Base64Url): '{}'",
    // searchKey);

    return credentialRepository.findByCredentialId(searchKey).map(this::toUserCredential)
        .orElse(null);
  }

  @Override
  public List<CredentialRecord> findByUserId(Bytes userId) {
    int id = Integer.parseInt(new String(userId.getBytes()));
    log.info("[DEBUG] Searching WebAuthn Credentials by userId: {}", id);
    return credentialRepository.findByUserId(id).stream().map(this::toUserCredential)
        .collect(Collectors.toList());
  }

  @Override
  public void save(CredentialRecord credential) {

    String credentialIdStr = credential.getCredentialId().toBase64UrlString();

    WebAuthnCredentialEntity entity = credentialRepository.findByCredentialId(credentialIdStr)
        .orElseGet(WebAuthnCredentialEntity::new);

    entity.setUserId(Integer.parseInt(new String(credential.getUserEntityUserId().getBytes())));
    entity.setCredentialId(credential.getCredentialId().toBase64UrlString());
    entity.setPublicKey(credential.getPublicKey().getBytes());
    entity.setCount(credential.getSignatureCount());
    entity.setAttestationObject(credential.getAttestationObject().getBytes());

    credentialRepository.save(entity);
  }

  @Override
  @Transactional
  public void delete(Bytes credentialId) {
    credentialRepository.deleteByCredentialId(credentialId.toBase64UrlString());
  }

  private CredentialRecord toUserCredential(WebAuthnCredentialEntity entity) {

    Bytes credentialIdBytes = Bytes.fromBase64(entity.getCredentialId());
    Bytes userIdBytes = new Bytes(
        String.valueOf(entity.getUserId()).getBytes(java.nio.charset.StandardCharsets.UTF_8));
    Bytes publicKeyBytes = new Bytes(entity.getPublicKey());

    // log.info("[DEBUG] toUserCredential - CredentialId (Base64Url): {}",
    // entity.getCredentialId());
    // log.info("[DEBUG] toUserCredential - UserId: {}", entity.getUserId());
    // log.info("[DEBUG] toUserCredential - PublicKey Length: {} bytes", entity.getPublicKey() !=
    // null ? entity.getPublicKey().length : 0);

    return ImmutableCredentialRecord.builder().credentialId(credentialIdBytes)
        .credentialType(PublicKeyCredentialType.PUBLIC_KEY)
        .publicKey(new ImmutablePublicKeyCose(publicKeyBytes.getBytes()))
        .userEntityUserId(userIdBytes).signatureCount(entity.getCount())
        .attestationObject(new Bytes(entity.getAttestationObject())).build();

  }

  // --- PublicKeyCredentialUserEntityRepository の実装 ---

  @Override
  public PublicKeyCredentialUserEntity findByUsername(String username) {
    return userRepository.findByName(username).map(this::toUserEntity).orElse(null);
  }

  @Override
  public PublicKeyCredentialUserEntity findById(Bytes userId) {
    // userId (Bytes) から UserEntity の ID (Integer) に変換して取得
    int id = Integer.parseInt(new String(userId.getBytes()));
    return userRepository.findById(id).map(this::toUserEntity).orElse(null);
  }

  @Override
  public void save(PublicKeyCredentialUserEntity userEntity) {
    // 既存の UserEntity (JPA) のプライマリキー(id)を WebAuthn ID としてそのまま流用しているため、
    // パキー登録時に新しい UserEntity を作成・保存する必要はありません。
    // 空実装でOKです。
  }

  private PublicKeyCredentialUserEntity toUserEntity(UserEntity user) {
    return ImmutablePublicKeyCredentialUserEntity.builder()
        .id(new Bytes(String.valueOf(user.getId()).getBytes())).name(user.getName())
        .displayName(user.getName()).build();
  }

}
