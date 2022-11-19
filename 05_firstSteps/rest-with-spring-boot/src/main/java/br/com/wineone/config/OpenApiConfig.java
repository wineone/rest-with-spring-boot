package br.com.wineone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	
	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI().
				info(new Info().title("restful api with java 18 with spring boot 3").
				version("v1").
				description("api para estudos").
				termsOfService("aaaaa").
				license(new License().name("Apache 2.0").url("aaaa")));
	}
}
