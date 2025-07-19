package com.cleber.cinema.config;

import com.cleber.cinema.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
				.authorizeHttpRequests(auth -> auth
						// Rotas públicas (GET de leitura geral)
						.requestMatchers(HttpMethod.GET, "/api/alimentos/**", "/api/cinemas/**", "/api/filmes/**", "/api/salas/**", "/api/sessoes/**", "/api/localidades/**").permitAll()

						// Rotas autenticadas (user ou admin)
						.requestMatchers("/api/pagamentos/**").authenticated()  // Todos os pagamentos requerem login
						.requestMatchers(HttpMethod.POST, "/api/localidades/**").authenticated()  // Criar localidade
						.requestMatchers(HttpMethod.PUT, "/api/localidades/**").authenticated()   // Atualizar localidade

						// Rotas admin-only
						.requestMatchers(HttpMethod.POST, "/api/alimentos/**", "/api/cinemas/**", "/api/filmes/**", "/api/salas/**", "/api/sessoes/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/alimentos/**", "/api/cinemas/**", "/api/filmes/**", "/api/salas/**", "/api/sessoes/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")  // Deletar qualquer coisa
						.requestMatchers("/api/admin/**").hasRole("ADMIN")  // Qualquer admin path

						// Rotas de dev e monitoramento
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**", "/actuator/**").permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						// Qualquer outra requer autenticação
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}