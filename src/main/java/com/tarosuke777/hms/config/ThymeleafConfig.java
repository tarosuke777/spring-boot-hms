package com.tarosuke777.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tarosuke777.hms.thymeleaf.dialect.HmsDialect;

@Configuration
public class ThymeleafConfig {
    @Bean
    public HmsDialect hmsDialect() {
        return new HmsDialect();
    }
}
