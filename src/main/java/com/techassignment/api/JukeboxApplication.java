package com.techassignment.api;

import com.techassignment.api.models.Jukebox;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@SpringBootApplication
public class JukeboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JukeboxApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
		return
				restTemplateBuilder
						.setConnectTimeout(Duration.ofMillis(3000))
						.setReadTimeout(Duration.ofMillis(3000))
						.build();
	}

	@Bean
	public ParameterizedTypeReference<List<Jukebox>> parameterizedTypeReference(){
		return new ParameterizedTypeReference<List<Jukebox>>() {};
	}
}
