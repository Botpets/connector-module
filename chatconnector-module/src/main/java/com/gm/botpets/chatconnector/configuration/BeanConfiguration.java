package com.gm.botpets.chatconnector.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.gm.botpets.chatconnector.commons.GracefulShutdown;

@Configuration
public class BeanConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(BeanConfiguration.class);

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public GracefulShutdown gracefulShutdown() {
		return new GracefulShutdown();
	}
}
