package com.tarosuke777.hms.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

class OpenApiConfigTest {

  @Test
  void testCustomOpenAPI() {
    OpenApiConfig openApiConfig = new OpenApiConfig();
    OpenAPI openAPI = openApiConfig.customOpenAPI();

    // Verify OpenAPI properties
    assertThat(openAPI).isNotNull();

    // Verify info
    assertThat(openAPI.getInfo()).isNotNull();
    assertThat(openAPI.getInfo().getTitle()).isEqualTo("My API");
    assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");

    // Verify servers configuration
    assertThat(openAPI.getServers()).isNotEmpty();
    assertThat(openAPI.getServers().get(0).getUrl()).isEqualTo("https://hms.home.arpa");
    assertThat(openAPI.getServers().get(0).getDescription()).isEqualTo("HTTPS");

    // Verify security scheme "bearerAuth"
    assertThat(openAPI.getComponents()).isNotNull();
    assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("bearerAuth");

    SecurityScheme bearerAuthScheme =
        openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
    assertThat(bearerAuthScheme).isNotNull();
    assertThat(bearerAuthScheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
    assertThat(bearerAuthScheme.getScheme()).isEqualTo("bearer");
    assertThat(bearerAuthScheme.getBearerFormat()).isEqualTo("JWT");

    // Verify global security requirement
    assertThat(openAPI.getSecurity()).isNotEmpty();
    SecurityRequirement globalSecurity = openAPI.getSecurity().get(0);
    assertThat(globalSecurity).containsKey("bearerAuth");
  }
}
