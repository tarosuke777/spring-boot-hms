package com.tarosuke777.hms.service;

import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.entity.WebAuthnCredentialEntity;
import com.tarosuke777.hms.repository.UserRepository;
import com.tarosuke777.hms.repository.WebAuthnCredentialRepository;

import java.nio.charset.StandardCharsets;
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
    return credentialRepository.findByCredentialId(searchKey).map(this::toUserCredential)
        .orElse(null);
  }

  @Override
  public List<CredentialRecord> findByUserId(Bytes userId) {
    int id = bytesToUserId(userId);
    return credentialRepository.findByUserId(id).stream().map(this::toUserCredential)
        .collect(Collectors.toList());
  }

  @Override
  public void save(CredentialRecord credential) {

    String credentialIdStr = credential.getCredentialId().toBase64UrlString();

    WebAuthnCredentialEntity entity = credentialRepository.findByCredentialId(credentialIdStr)
        .orElseGet(WebAuthnCredentialEntity::new);

    entity.setUserId(bytesToUserId(credential.getUserEntityUserId()));
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

  // --- PublicKeyCredentialUserEntityRepository の実装 ---

  @Override
  public PublicKeyCredentialUserEntity findByUsername(String username) {
    return userRepository.findByName(username).map(this::toUserEntity).orElse(null);
  }

  @Override
  public PublicKeyCredentialUserEntity findById(Bytes userId) {
    int id = bytesToUserId(userId);
    return userRepository.findById(id).map(this::toUserEntity).orElse(null);
  }

  @Override
  public void save(PublicKeyCredentialUserEntity userEntity) {
    // 既存の UserEntity (JPA) の ID をそのまま流用するため処理不要
  }


  // --- 変換ヘルパーメソッド ---

  private CredentialRecord toUserCredential(WebAuthnCredentialEntity entity) {

    Bytes credentialIdBytes = Bytes.fromBase64(entity.getCredentialId());
    Bytes userIdBytes = userIdToBytes(entity.getUserId());
    Bytes publicKeyBytes = new Bytes(entity.getPublicKey());

    return ImmutableCredentialRecord.builder().credentialId(credentialIdBytes)
        .credentialType(PublicKeyCredentialType.PUBLIC_KEY)
        .publicKey(new ImmutablePublicKeyCose(publicKeyBytes.getBytes()))
        .userEntityUserId(userIdBytes).signatureCount(entity.getCount())
        .attestationObject(new Bytes(entity.getAttestationObject())).build();

  }

  private PublicKeyCredentialUserEntity toUserEntity(UserEntity user) {
    return ImmutablePublicKeyCredentialUserEntity.builder().id(userIdToBytes(user.getId()))
        .name(user.getName()).displayName(user.getName()).build();
  }

  private Bytes userIdToBytes(Integer userId) {
    return new Bytes(String.valueOf(userId).getBytes(StandardCharsets.UTF_8));
  }

  private Integer bytesToUserId(Bytes bytes) {
    String idStr = new String(bytes.getBytes(), StandardCharsets.UTF_8);
    return Integer.parseInt(idStr);
  }

}
