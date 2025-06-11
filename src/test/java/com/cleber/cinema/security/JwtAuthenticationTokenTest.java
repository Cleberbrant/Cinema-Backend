package com.cleber.cinema.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationTokenTest {

	@Test
	void construtor_DeveSetarCamposCorretamente() {
		String principal = "user123";
		String credentials = "token-abc";
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

		JwtAuthenticationToken token = new JwtAuthenticationToken(principal, credentials, authorities);

		assertEquals(principal, token.getPrincipal());
		assertEquals(credentials, token.getCredentials());
		assertTrue(token.isAuthenticated());
		assertEquals(1, token.getAuthorities().size());
		assertEquals("ROLE_USER", token.getAuthorities().iterator().next().getAuthority());
	}

	@Test
	void construtor_SemAuthorities_DevePermitirListaVazia() {
		String principal = "user123";
		String credentials = "token-abc";

		JwtAuthenticationToken token = new JwtAuthenticationToken(principal, credentials, Collections.emptyList());

		assertEquals(principal, token.getPrincipal());
		assertEquals(credentials, token.getCredentials());
		assertTrue(token.getAuthorities().isEmpty());
		assertTrue(token.isAuthenticated());
	}
}
