package com.cleber.cinema.controller;

import com.cleber.cinema.dto.SessaoCreateDTO;
import com.cleber.cinema.dto.SessaoDTO;
import com.cleber.cinema.services.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessoes")
@Tag(name = "Sessões", description = "Gerenciamento de sessões de cinema")
@RequiredArgsConstructor
public class SessaoController {

	private final SessaoService service;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar sessão")
	public ResponseEntity<SessaoDTO> create(@Valid @RequestBody SessaoCreateDTO dto) {
		SessaoDTO created = service.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<SessaoDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<SessaoDTO> findById(@PathVariable Integer id) {
		SessaoDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar sessão")
	public ResponseEntity<SessaoDTO> update(@PathVariable Integer id, @Valid @RequestBody SessaoCreateDTO dto) {
		SessaoDTO updated = service.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir sessão")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/filme/{filmeId}")
	@Operation(summary = "Buscar por filme")
	public ResponseEntity<List<SessaoDTO>> findByFilme(@PathVariable Integer filmeId) {
		return ResponseEntity.ok(service.findByFilme(filmeId));
	}

	@GetMapping("/sala/{salaId}")
	@Operation(summary = "Buscar por sala")
	public ResponseEntity<List<SessaoDTO>> findBySala(@PathVariable Integer salaId) {
		return ResponseEntity.ok(service.findBySala(salaId));
	}

	@GetMapping("/data/{data}")
	@Operation(summary = "Buscar por data")
	public ResponseEntity<List<SessaoDTO>> findByData(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
		return ResponseEntity.ok(service.findByData(data));
	}
}