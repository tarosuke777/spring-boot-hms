package com.tarosuke777.hms.config;

import com.tarosuke777.hms.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${webauthn.rp-name}")
  private String rpName;

  @Value("${webauthn.rp-id}")
  private String rpId;

  @Value("${webauthn.allowed-origins}")
  private String allowedOrigins;

  @Bean
  @Order(3)
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**") // /api/ で始まるリクエストにだけ適用
        .csrf(csrf -> csrf.disable()) // APIなのでCSRFはオフでOK
        .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults()); // Basic認証を有効化

    return http.build();
  }

  @Bean
  @Order(4)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login.loginProcessingUrl("/login").loginPage("/login")
        .usernameParameter("userName").passwordParameter("password").defaultSuccessUrl("/top", true)
        .failureUrl("/login?error").permitAll());

    http.logout(logout -> logout.logoutSuccessUrl("/login"));

    http.webAuthn(webAuthn -> webAuthn.rpName(rpName) // ユーザーのデバイスに表示されるアプリ名
        .rpId(rpId) // ドメイン名
        .allowedOrigins(allowedOrigins) // アクセスを許可するオリジン
    );

    http.authorizeHttpRequests(
        (authz) -> authz.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .permitAll().requestMatchers("/user/signup").permitAll()
            .requestMatchers("/webauthn/**", "/login/webauthn/**").permitAll()
            .requestMatchers("/user/**").hasRole(Role.ADMIN.name()).anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
