package com.tarosuke777.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Order(4)
public class McpSecurityConifg {
    @Bean
    @Order(2)
    SecurityFilterChain mcpSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/sse/**", "/mcp/**");
        http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll());
        http.csrf(CsrfConfigurer::disable);
        return http.build();
    }
}
