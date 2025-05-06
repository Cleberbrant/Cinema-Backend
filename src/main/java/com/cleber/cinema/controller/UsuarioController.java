package com.cleber.cinema.controller;

import com.cleber.cinema.dto.UsuarioDTO;
import com.cleber.cinema.dto.UsuarioCreateDTO;
import com.cleber.cinema.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;

	// Cadastro de usuário (aberto)
	@PostMapping("/register")
	public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody UsuarioCreateDTO dto) {
		return ResponseEntity.ok(usuarioService.cadastrar(dto));
	}

	// Listar todos os usuários (apenas para ADMIN)
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
		List<UsuarioDTO> usuarios = usuarioService.listarTodos();
		return ResponseEntity.ok(usuarios);
	}

	// Buscar usuário por ID (apenas para ADMIN)
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
		return usuarioService.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Deletar usuário por ID (apenas para ADMIN)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			usuarioService.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Atualizar usuário por ID (apenas para ADMIN)
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
		try {
			UsuarioDTO atualizado = usuarioService.atualizar(id, usuarioDTO);
			return ResponseEntity.ok(atualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Promover usuário para admin (apenas para ADMIN)
	@PutMapping("/{id}/promover")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioDTO> promoverParaAdmin(@PathVariable Long id) {
		UsuarioDTO promovido = usuarioService.promoverParaAdmin(id);
		return ResponseEntity.ok(promovido);
	}
}