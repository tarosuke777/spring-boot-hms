package com.tarosuke777.hms.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

@Profile("local")
@Configuration
@EnableWebSecurity
@Order(2)
/**
 * https://spring.pleiades.io/spring-boot/reference/data/sql.html#data.sql.h2-web-console
 */
public class LocalSecurityConfig {
  @Bean
  SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(PathRequest.toH2Console());
    http.authorizeHttpRequests(
        (authz) ->
            authz
                .requestMatchers(PathRequest.toH2Console())
                .permitAll()
                .anyRequest()
                .authenticated());
    http.csrf(CsrfConfigurer::disable);
    http.headers((headers) -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
    return http.build();
  }
}
