package com.tarosuke777.hms.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.tarosuke777.hms.domain.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(
        login ->
            login
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .usernameParameter("userId")
                .passwordParameter("password")
                .defaultSuccessUrl("/music/list",true)
                .failureUrl("/login?error")
                .permitAll());

    http.logout(logout -> logout.logoutSuccessUrl("/login"));

    http.authorizeHttpRequests(
        (authz) ->
            authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/user/signup").permitAll()
                .requestMatchers("/user/**").hasAuthority("ROLE_ADMIN")
                .anyRequest()
                .authenticated());

    return http.build();
  }

  @Bean
  UserDetailsService userDetailsService() {
    return new UserDetailServiceImpl();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
