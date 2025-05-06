package com.cleber.cinema.controller;

import com.cleber.cinema.dto.AuthRequestDTO;
import com.cleber.cinema.dto.AuthResponseDTO;
import com.cleber.cinema.dto.UsuarioDTO;
import com.cleber.cinema.model.Usuario;
import com.cleber.cinema.repositories.UsuarioRepository;
import com.cleber.cinema.security.JwtUtil;
import com.cleber.cinema.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthResponseDTO(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UsuarioDTO usuarioDTO) {
		// Verifique se o email já existe
		if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Email já cadastrado");
		}
		// Crie um novo usuário
		Usuario usuario = Usuario.builder()
				.email(usuarioDTO.getEmail())
				.password(passwordEncoder.encode(usuarioDTO.getPassword()))
				.role(usuarioDTO.getRole() != null ? usuarioDTO.getRole() : "ROLE_CLIENTE")
				.build();
		usuarioRepository.save(usuario);
		return ResponseEntity.ok("Usuário cadastrado com sucesso!");
	}

}
