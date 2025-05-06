package com.cleber.cinema.controller;

import com.cleber.cinema.dto.AuthRequestDTO;
import com.cleber.cinema.dto.AuthResponseDTO;
import com.cleber.cinema.dto.UsuarioCreateDTO;
import com.cleber.cinema.enums.Role;
import com.cleber.cinema.model.Localidade;
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
	public ResponseEntity<?> register(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
		if (usuarioRepository.findByEmail(usuarioCreateDTO.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
		}
		Role role = Role.ROLE_USER;
		if (usuarioCreateDTO.getRole() != null) {
			try {
				role = Role.valueOf(usuarioCreateDTO.getRole());
			} catch (IllegalArgumentException e) {
				return ResponseEntity.badRequest().body("Role inválido! Use apenas: ROLE_USER ou ROLE_ADMIN.");
			}
		}
		Usuario usuario = Usuario.builder()
				.email(usuarioCreateDTO.getEmail())
				.password(passwordEncoder.encode(usuarioCreateDTO.getPassword()))
				.nome(usuarioCreateDTO.getNome())
				.dataNascimento(usuarioCreateDTO.getDataNascimento())
				.cpf(usuarioCreateDTO.getCpf())
				.role(role)
				.localidade(usuarioCreateDTO.getLocalidade() != null
								? new Localidade(
								null,
								usuarioCreateDTO.getLocalidade().getEndereco(),
								usuarioCreateDTO.getLocalidade().getCep(),
								usuarioCreateDTO.getLocalidade().getReferencia()
						)
								: null
				)
				.build();
		usuarioRepository.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
	}
}