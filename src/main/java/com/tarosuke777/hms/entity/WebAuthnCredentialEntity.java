package com.tarosuke777.hms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "webauthn_credentials")
@Getter
@Setter
public class WebAuthnCredentialEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // どのユーザーのパスキーか (UserEntityのID)
  @Column(nullable = false)
  private Integer userId;

  // クレデンシャルID（WebAuthn固有の鍵識別子）
  @Lob
  @Column(nullable = false, unique = true)
  private String credentialId;

  // 公開鍵データ
  @Lob
  @Column(nullable = false)
  private byte[] publicKey;

  // 認証カウンター（リプレイ攻撃防止用）
  private long count;

  // デバイスの表示名（例: "Tarou's iPhone"）
  private String label;

  @Lob
  @Column(nullable = false)
  private byte[] attestationObject;

}
