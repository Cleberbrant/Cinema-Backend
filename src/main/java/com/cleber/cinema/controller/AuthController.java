package com.cleber.cinema.controller;

import com.cleber.cinema.dto.AuthRequestDTO;
import com.cleber.cinema.dto.AuthResponseDTO;
import com.cleber.cinema.dto.UsuarioCadastroDTO;
import com.cleber.cinema.enums.Role;
import com.cleber.cinema.model.Usuario;
import com.cleber.cinema.repositories.UsuarioRepository;
import com.cleber.cinema.security.JwtUtil;
import com.cleber.cinema.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<?> register(@RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
		if (usuarioRepository.findByEmail(usuarioCadastroDTO.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
		}
		Role role = Role.ROLE_USER;
		if (usuarioCadastroDTO.getRole() != null) {
			try {
				role = Role.valueOf(usuarioCadastroDTO.getRole());
			} catch (IllegalArgumentException e) {
				return ResponseEntity.badRequest().body("Role inválido! Use apenas: ROLE_USER ou ROLE_ADMIN.");
			}
		}
		Usuario usuario = Usuario.builder()
				.email(usuarioCadastroDTO.getEmail())
				.password(passwordEncoder.encode(usuarioCadastroDTO.getPassword()))
				.nome(usuarioCadastroDTO.getNome())
				.dataNascimento(usuarioCadastroDTO.getDataNascimento())
				.cpf(usuarioCadastroDTO.getCpf())
				.endereco(usuarioCadastroDTO.getEndereco())
				.estado(usuarioCadastroDTO.getEstado())
				.cidade(usuarioCadastroDTO.getCidade())
				.role(role)
				.build();
		usuarioRepository.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
	}
}