package com.cleber.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// Origens permitidas
		configuration.setAllowedOriginPatterns(Arrays.asList(
				"http://localhost:*",
				"http://127.0.0.1:*"
		));

		// Métodos HTTP permitidos
		configuration.setAllowedMethods(Arrays.asList(
				"GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
		));

		// Headers permitidos
		configuration.setAllowedHeaders(Arrays.asList("*"));

		// Headers expostos
		configuration.setExposedHeaders(Arrays.asList(
				"Authorization", "Content-Type", "X-Total-Count"
		));

		// Permitir credenciais
		configuration.setAllowCredentials(true);

		// Cache do preflight
		configuration.setMaxAge(3600L);

		// Aplicar para todas as rotas
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}