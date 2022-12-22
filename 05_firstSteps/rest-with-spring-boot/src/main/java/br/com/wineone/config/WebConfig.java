package br.com.wineone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${cors.originPatterns:default}")
	private String corsOriginPatterns = "";
	
	

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		var allowedOriginPatterns = corsOriginPatterns.split(",");
		registry.addMapping("/**")
//		.allowedMethods("GET","POST","PUT")
		.allowedMethods("*")
		.allowedOriginPatterns(allowedOriginPatterns)
		.allowCredentials(true);
		
	}



	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// Via query parameter
//		configurer.favorParameter(true).
//		parameterName("mediaType").
//		ignoreAcceptHeader(true).
//		useRegisteredExtensionsOnly(false).
//		defaultContentType(MediaType.APPLICATION_JSON).
//		mediaType("json",MediaType.APPLICATION_JSON).
//		mediaType("xml",MediaType.APPLICATION_XML);
		
		// via header
		configurer.favorParameter(false).
		ignoreAcceptHeader(false).
		useRegisteredExtensionsOnly(false).
		defaultContentType(MediaType.APPLICATION_JSON).
		mediaType("json",MediaType.APPLICATION_JSON).
		mediaType("xml",MediaType.APPLICATION_XML);
	}
	
}
