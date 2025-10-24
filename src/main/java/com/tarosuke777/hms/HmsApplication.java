package com.tarosuke777.hms;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.tarosuke777.hms.domain.MusicService;
import com.tarosuke777.hms.domain.TrainingService;

@SpringBootApplication
public class HmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmsApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider allTools(MusicService musicService,
			TrainingService trainingService) {
		return MethodToolCallbackProvider.builder().toolObjects(musicService, trainingService)
				.build();
	}
}
