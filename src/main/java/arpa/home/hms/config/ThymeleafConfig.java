package arpa.home.hms.config;

import arpa.home.hms.thymeleaf.dialect.HmsDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
  @Bean
  public HmsDialect hmsDialect() {
    return new HmsDialect();
  }
}
