package com.cleber.cinema.services;

import com.cleber.cinema.model.Usuario;
import com.cleber.cinema.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
		// Log para debug
		System.out.println("Usuário autenticado: " + usuario.getEmail() + " | Role: " + usuario.getRole());

		// Corrigido: converte enum Role para String usando .name()
		return User.builder()
				.username(usuario.getEmail())
				.password(usuario.getPassword())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole().name())))
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}
}