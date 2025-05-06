package com.cleber.cinema.controller;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.services.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@Tag(name = "Cinemas", description = "Gerenciamento de cinemas")
@RequiredArgsConstructor
public class CinemaController {

	private final CinemaService service;

	// Criar cinema (apenas ADMIN)
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar cinema")
	public ResponseEntity<CinemaDTO> create(@Valid @RequestBody CinemaCreateDTO dto) {
		return ResponseEntity.status(201).body(service.create(dto));
	}

	// Listar todos (aberto)
	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<CinemaDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	// Buscar por ID (aberto)
	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<CinemaDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	// Atualizar cinema (apenas ADMIN)
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar cinema")
	public ResponseEntity<CinemaDTO> update(@PathVariable Integer id, @Valid @RequestBody CinemaCreateDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	// Excluir cinema (apenas ADMIN)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir cinema")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Buscar por nome (aberto)
	@GetMapping("/nome/{nome}")
	@Operation(summary = "Buscar por nome")
	public ResponseEntity<List<CinemaDTO>> findByNome(@PathVariable String nome) {
		return ResponseEntity.ok(service.findByNome(nome));
	}
}