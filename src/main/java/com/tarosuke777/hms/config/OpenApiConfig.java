package com.tarosuke777.hms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
        .servers(List.of(new Server().url("https://hms.home.arpa").description("HTTPS"),
            new Server().url("http://localhost:8080").description("localhost HTTP")))
        // タイトルやバージョン情報
        .info(new Info().title("My API").version("1.0.0"))
        // 1. JWTのセキュリティスキームを定義
        .components(new Components().addSecuritySchemes(securitySchemeName,
            new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP)
                .scheme("bearer").bearerFormat("JWT")))
        // 2. 定義したセキュリティをグローバル（全エンドポイント）に適用
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
  }
}
