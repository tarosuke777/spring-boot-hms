package com.tarosuke777.hms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("$jwt.secret}")
  private String secretKeyString;

  @Value("${jwt.access-token-expiration-ms}")
  private long accessTokenExpirationMs;

  @Value("${jwt.refresh-token-expiration-ms}")
  private long refreshTokenExpirationMs;

  private SecretKey secretKey;

  @PostConstruct
  protected void init() {
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Access Token の生成
   */
  public String generateAccessToken(String username) {
    return generateToken(username, accessTokenExpirationMs);
  }

  /**
   * Refresh Token の生成
   */
  public String generateRefreshToken(String username) {
    return generateToken(username, refreshTokenExpirationMs);
  }

  private String generateToken(String username, long expirationMs) {

    Instant now = Instant.now();
    Instant expiryDate = now.plus(expirationMs, ChronoUnit.MILLIS);

    return Jwts.builder().subject(username).issuedAt(Date.from(now))
        .expiration(Date.from(expiryDate)).signWith(secretKey).compact();
  }

  /**
   * トークンからユーザー名を取り出し、Spring Security の認証オブジェクトを生成
   */
  public Authentication getAuthentication(String token, UserDetailsService userDetailsService) {
    String username = getUsername(token);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  /**
   * トークンから Subject (ユーザー名) を取得
   */
  public String getUsername(String token) {
    Claims claims =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }

  /**
   * トークンの有効性・署名・期限切れチェック
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      // 期限切れ、改ざん、フォーマット不正などの場合は false
      return false;
    }
  }
}
