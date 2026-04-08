package com.tarosuke777.hms.config;

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
import com.tarosuke777.hms.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
        .usernameParameter("userName").passwordParameter("password")
        .defaultSuccessUrl("/music/list", true).failureUrl("/login?error").permitAll());

    http.logout(logout -> logout.logoutSuccessUrl("/login"));

    http.authorizeHttpRequests(
        (authz) -> authz.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .permitAll().requestMatchers("/user/signup").permitAll().requestMatchers("/user/**")
            .hasRole(Role.ADMIN.name()).anyRequest().authenticated());

    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
