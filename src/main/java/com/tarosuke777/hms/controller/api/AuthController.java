package com.tarosuke777.hms.controller.api;

import com.tarosuke777.hms.security.JwtTokenProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // ID / パスワードの認証実行
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    String username = authentication.getName();
    String accessToken = tokenProvider.generateAccessToken(username);
    String refreshToken = tokenProvider.generateRefreshToken(username);

    return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
  }

  public record LoginRequest(String username, String password) {}
}
