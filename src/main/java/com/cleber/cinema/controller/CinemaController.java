package com.cleber.cinema.controller;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.services.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar cinema")
	public ResponseEntity<CinemaDTO> create(@Valid @RequestBody CinemaCreateDTO dto) {
		CinemaDTO created = service.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<CinemaDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<CinemaDTO> findById(@PathVariable Integer id) {
		CinemaDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar cinema")
	public ResponseEntity<CinemaDTO> update(@PathVariable Integer id, @Valid @RequestBody CinemaCreateDTO dto) {
		CinemaDTO updated = service.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir cinema")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/nome/{nome}")
	@Operation(summary = "Buscar por nome")
	public ResponseEntity<List<CinemaDTO>> findByNome(@PathVariable String nome) {
		return ResponseEntity.ok(service.findByNome(nome));
	}
}