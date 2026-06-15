package com.tarosuke777.hms.config;

import java.time.Duration;
import org.springframework.ai.model.ollama.autoconfigure.OllamaConnectionProperties;
import org.springframework.ai.model.ollama.autoconfigure.OllamaEmbeddingProperties;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import io.micrometer.observation.ObservationRegistry;

@Configuration
@EnableConfigurationProperties({OllamaConnectionProperties.class, OllamaEmbeddingProperties.class})
public class OllamaConfig {

    @Bean
    public OllamaEmbeddingModel ollamaEmbeddingModel(
            OllamaConnectionProperties connectionProperties,
            OllamaEmbeddingProperties embeddingProperties, ObservationRegistry observationRegistry,
            ModelManagementOptions modelManagementOptions) {

        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(3)) // 接続タイムアウト: 3秒
                .withReadTimeout(Duration.ofSeconds(30)); // 読み込みタイムアウト: 30秒

        ClientHttpRequestFactory requestFactory =
                ClientHttpRequestFactoryBuilder.detect().build(settings);

        RestClient.Builder restClientBuilder = RestClient.builder().requestFactory(requestFactory);

        OllamaApi ollamaApi = OllamaApi.builder().baseUrl(connectionProperties.getBaseUrl())
                .restClientBuilder(restClientBuilder).build();

        return OllamaEmbeddingModel.builder().ollamaApi(ollamaApi)
                .defaultOptions(embeddingProperties.getOptions())
                .observationRegistry(observationRegistry)
                .modelManagementOptions(modelManagementOptions).build();
    }
}
