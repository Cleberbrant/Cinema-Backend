package com.cleber.cinema.controller;

import com.cleber.cinema.dto.SalaCreateDTO;
import com.cleber.cinema.dto.SalaDTO;
import com.cleber.cinema.services.SalaService;
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
@RequestMapping("/api/salas")
@Tag(name = "Salas", description = "Gerenciamento de salas de cinema")
@RequiredArgsConstructor
public class SalaController {

	private final SalaService service;

	// Criar sala (apenas ADMIN)
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar sala")
	public ResponseEntity<SalaDTO> create(@Valid @RequestBody SalaCreateDTO dto) {
		return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
	}

	// Listar todas (aberto)
	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<SalaDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	// Buscar por ID (aberto)
	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<SalaDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	// Atualizar sala (apenas ADMIN)
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar sala")
	public ResponseEntity<SalaDTO> update(@PathVariable Integer id, @Valid @RequestBody SalaCreateDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	// Excluir sala (apenas ADMIN)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir sala")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Buscar por cinema (aberto)
	@GetMapping("/cinema/{cinemaId}")
	@Operation(summary = "Buscar por cinema")
	public ResponseEntity<List<SalaDTO>> findByCinema(@PathVariable Integer cinemaId) {
		return ResponseEntity.ok(service.findByCinema(cinemaId));
	}

	// Buscar por tecnologia (aberto)
	@GetMapping("/tecnologia/{tecnologia}")
	@Operation(summary = "Buscar por tecnologia")
	public ResponseEntity<List<SalaDTO>> findByTecnologia(@PathVariable String tecnologia) {
		return ResponseEntity.ok(service.findByTecnologia(tecnologia));
	}
}