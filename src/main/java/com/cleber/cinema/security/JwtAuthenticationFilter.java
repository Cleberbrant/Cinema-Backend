package com.cleber.cinema.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();

		// ⚠️ LOGS PARA DEBUGGING
		System.out.println("=== JWT FILTER CINEMA-BACKEND ===");
		System.out.println("URI: " + requestURI);
		System.out.println("Method: " + method);

		log.debug("=== JWT FILTER - Processando requisição: {} {} ===", method, requestURI);

		// PRIMEIRO: Verificar se é rota pública
		if (isPublicRoute(requestURI, method)) {
			System.out.println("=== ROTA PÚBLICA DETECTADA - PASSANDO DIRETO ===");
			log.debug("=== ROTA PÚBLICA DETECTADA: {} {} - PASSANDO SEM AUTENTICAÇÃO ===", method, requestURI);
			filterChain.doFilter(request, response);
			return;
		}

		System.out.println("=== ROTA PROTEGIDA - VERIFICANDO TOKEN ===");

		// SEGUNDO: Verificar header Authorization
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			System.out.println("=== TOKEN AUSENTE - UNAUTHORIZED ===");
			log.debug("=== TOKEN NÃO ENCONTRADO - Requisição para rota protegida sem token ===");

			// Retornar erro 401 diretamente para rotas protegidas sem token
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"error\": \"Unauthorized - Token ausente ou inválido\"}");
			return;
		}

		String token = header.substring(7);
		System.out.println("=== TOKEN ENCONTRADO - VALIDANDO ===");
		log.debug("=== TOKEN ENCONTRADO: {} ===", token.substring(0, Math.min(token.length(), 20)) + "...");

		try {
			// Usar a mesma chave secreta do auth-cinema
			SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();

			String userEmail = claims.getSubject();
			String role = claims.get("role", String.class);

			System.out.println("=== TOKEN VÁLIDO - Email: " + userEmail + " | Role: " + role + " ===");
			log.debug("=== TOKEN VÁLIDO - Email: {} | Role: {} ===", userEmail, role);

			// Garantir que role tenha o prefixo ROLE_
			String normalizedRole = role != null && !role.startsWith("ROLE_") ? "ROLE_" + role : role;

			// Criar authorities baseado na role
			List<SimpleGrantedAuthority> authorities = normalizedRole != null ?
					List.of(new SimpleGrantedAuthority(normalizedRole)) :
					Collections.emptyList();

			// Criar Authentication object
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			// Definir no SecurityContext
			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.debug("=== AUTENTICAÇÃO DEFINIDA NO SECURITY CONTEXT ===");

		} catch (Exception e) {
			System.out.println("=== ERRO AO PROCESSAR TOKEN: " + e.getMessage() + " ===");
			log.error("=== ERRO AO PROCESSAR TOKEN: {} ===", e.getMessage(), e);  // Adicionado: loga stack trace completo
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"error\": \"Unauthorized - Token inválido: " + e.getMessage() + "\"}");
			SecurityContextHolder.clearContext();
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isPublicRoute(String requestURI, String method) {
		System.out.println("=== VERIFICANDO SE É ROTA PÚBLICA ===");
		System.out.println("URI para verificar: " + requestURI);
		System.out.println("Method para verificar: " + method);

		log.debug("=== VERIFICANDO SE É ROTA PÚBLICA: {} {} ===", method, requestURI);

		// Rotas administrativas e Swagger sempre públicas (sem mudanças)
		if (requestURI.startsWith("/swagger-ui/") ||
				requestURI.startsWith("/v3/api-docs/") ||
				requestURI.equals("/v3/api-docs") ||
				requestURI.startsWith("/swagger-resources/") ||
				requestURI.startsWith("/actuator/") ||
				requestURI.startsWith("/h2-console/") ||
				requestURI.endsWith(".css") ||
				requestURI.endsWith(".js") ||
				requestURI.endsWith(".png") ||
				requestURI.endsWith(".svg")) {
			System.out.println("=== ROTA ADMINISTRATIVA/SWAGGER PÚBLICA ===");
			log.debug("=== ROTA ADMINISTRATIVA/SWAGGER PÚBLICA ===");
			return true;
		}

		// OPTIONS sempre público (CORS preflight)
		if ("OPTIONS".equals(method)) {
			System.out.println("=== ROTA OPTIONS PÚBLICA ===");
			log.debug("=== ROTA OPTIONS PÚBLICA ===");
			return true;
		}

		// GET rotas públicas específicas (ajustado para paths raiz e subpaths)
		if ("GET".equals(method)) {
			boolean isPublicGet =
					// Para Alimentos
					requestURI.equals("/api/alimentos") || requestURI.startsWith("/api/alimentos/") ||
							// Para Cinemas
							requestURI.equals("/api/cinemas") || requestURI.startsWith("/api/cinemas/") ||
							// Para Filmes
							requestURI.equals("/api/filmes") || requestURI.startsWith("/api/filmes/") ||
							// Para Salas
							requestURI.equals("/api/salas") || requestURI.startsWith("/api/salas/") ||
							// Para Sessoes
							requestURI.equals("/api/sessoes") || requestURI.startsWith("/api/sessoes/") ||
							// Para Localidades
							requestURI.equals("/api/localidades") || requestURI.startsWith("/api/localidades/");

			System.out.println("=== GET REQUEST - isPublicGet: " + isPublicGet + " ===");

			if (isPublicGet) {
				System.out.println("=== ROTA GET PÚBLICA CONFIRMADA ===");
				log.debug("=== ROTA GET PÚBLICA: {} ===", requestURI);
				return true;
			}
		}

		System.out.println("=== ROTA PROTEGIDA CONFIRMADA ===");
		log.debug("=== ROTA PROTEGIDA: {} {} ===", method, requestURI);
		return false;
	}
}
