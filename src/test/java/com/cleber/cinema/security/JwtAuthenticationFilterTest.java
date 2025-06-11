package com.cleber.cinema.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private JwtAuthenticationFilter filter;

	private final String jwtSecret = "testSecret";

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(filter, "jwtSecret", jwtSecret);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void doFilterInternal_SemHeaderAuthorization_DeveContinuarFilterChain() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(null);

		filter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verify(securityContext, never()).setAuthentication(any());
	}

	@Test
	void doFilterInternal_HeaderSemBearer_DeveContinuarFilterChain() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn("Basic abc123");

		filter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verify(securityContext, never()).setAuthentication(any());
	}

	@Test
	void doFilterInternal_TokenValido_DeveAutenticarUsuario() throws ServletException, IOException {
		String token = "validToken";
		String authHeader = "Bearer " + token;

		Claims claims = new DefaultClaims();
		claims.setSubject("user123");
		claims.put("role", "ROLE_USER");

		when(request.getHeader("Authorization")).thenReturn(authHeader);

		try (MockedStatic<Jwts> jwtsMock = mockStatic(Jwts.class)) {
			var parserMock = mock(io.jsonwebtoken.JwtParser.class);
			var jwsMock = mock(io.jsonwebtoken.Jws.class);

			jwtsMock.when(Jwts::parser).thenReturn(parserMock);
			when(parserMock.setSigningKey(jwtSecret)).thenReturn(parserMock);
			when(parserMock.parseClaimsJws(token)).thenReturn(jwsMock);
			when(jwsMock.getBody()).thenReturn(claims);

			filter.doFilterInternal(request, response, filterChain);

			verify(securityContext).setAuthentication(any());
			verify(filterChain).doFilter(request, response);
		}
	}

	@Test
	void doFilterInternal_TokenValidoSemRole_DeveAutenticarSemAuthorities() throws ServletException, IOException {
		String token = "validToken";
		String authHeader = "Bearer " + token;

		Claims claims = new DefaultClaims();
		claims.setSubject("user123");
		// Sem role

		when(request.getHeader("Authorization")).thenReturn(authHeader);

		try (MockedStatic<Jwts> jwtsMock = mockStatic(Jwts.class)) {
			var parserMock = mock(io.jsonwebtoken.JwtParser.class);
			var jwsMock = mock(io.jsonwebtoken.Jws.class);

			jwtsMock.when(Jwts::parser).thenReturn(parserMock);
			when(parserMock.setSigningKey(jwtSecret)).thenReturn(parserMock);
			when(parserMock.parseClaimsJws(token)).thenReturn(jwsMock);
			when(jwsMock.getBody()).thenReturn(claims);

			filter.doFilterInternal(request, response, filterChain);

			verify(securityContext).setAuthentication(any());
			verify(filterChain).doFilter(request, response);
		}
	}

	@Test
	void doFilterInternal_TokenInvalido_DeveRetornar401() throws ServletException, IOException {
		String token = "invalidToken";
		String authHeader = "Bearer " + token;

		when(request.getHeader("Authorization")).thenReturn(authHeader);

		try (MockedStatic<Jwts> jwtsMock = mockStatic(Jwts.class)) {
			var parserMock = mock(io.jsonwebtoken.JwtParser.class);

			jwtsMock.when(Jwts::parser).thenReturn(parserMock);
			when(parserMock.setSigningKey(jwtSecret)).thenReturn(parserMock);
			when(parserMock.parseClaimsJws(token)).thenThrow(new RuntimeException("Token inválido"));

			filter.doFilterInternal(request, response, filterChain);

			verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			verify(securityContext, never()).setAuthentication(any());
			verify(filterChain, never()).doFilter(request, response);
		}
	}

	@Test
	void doFilterInternal_ExcecaoNoParsing_DeveRetornar401() throws ServletException, IOException {
		String token = "malformedToken";
		String authHeader = "Bearer " + token;

		when(request.getHeader("Authorization")).thenReturn(authHeader);

		try (MockedStatic<Jwts> jwtsMock = mockStatic(Jwts.class)) {
			jwtsMock.when(Jwts::parser).thenThrow(new RuntimeException("Erro no parser"));

			filter.doFilterInternal(request, response, filterChain);

			verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			verify(securityContext, never()).setAuthentication(any());
			verify(filterChain, never()).doFilter(request, response);
		}
	}
}
