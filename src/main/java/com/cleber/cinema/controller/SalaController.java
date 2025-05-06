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

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar sala")
	public ResponseEntity<SalaDTO> create(@Valid @RequestBody SalaCreateDTO dto) {
		SalaDTO created = service.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<SalaDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<SalaDTO> findById(@PathVariable Integer id) {
		SalaDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar sala")
	public ResponseEntity<SalaDTO> update(@PathVariable Integer id, @Valid @RequestBody SalaCreateDTO dto) {
		SalaDTO updated = service.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir sala")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cinema/{cinemaId}")
	@Operation(summary = "Buscar por cinema")
	public ResponseEntity<List<SalaDTO>> findByCinema(@PathVariable Integer cinemaId) {
		return ResponseEntity.ok(service.findByCinema(cinemaId));
	}

	@GetMapping("/tecnologia/{tecnologia}")
	@Operation(summary = "Buscar por tecnologia")
	public ResponseEntity<List<SalaDTO>> findByTecnologia(@PathVariable String tecnologia) {
		return ResponseEntity.ok(service.findByTecnologia(tecnologia));
	}
}