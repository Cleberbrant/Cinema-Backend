package com.cleber.cinema.controller;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.services.LocalidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@Tag(name = "Localidades", description = "Gerenciamento de localidades")
@RequiredArgsConstructor
public class LocalidadeController {

	private final LocalidadeService service;

	// Criar localidade (apenas ADMIN)
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar localidade")
	public ResponseEntity<LocalidadeDTO> create(@Valid @RequestBody LocalidadeCreateDTO dto) {
		return ResponseEntity.status(201).body(service.create(dto));
	}

	// Listar todas (aberto)
	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<LocalidadeDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	// Buscar por ID (aberto)
	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<LocalidadeDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	// Atualizar localidade (apenas ADMIN)
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar localidade")
	public ResponseEntity<LocalidadeDTO> update(@PathVariable Integer id, @Valid @RequestBody LocalidadeCreateDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	// Excluir localidade (apenas ADMIN)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir localidade")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Buscar por CEP (aberto)
	@GetMapping("/cep/{cep}")
	@Operation(summary = "Buscar por CEP")
	public ResponseEntity<List<LocalidadeDTO>> findByCep(@PathVariable String cep) {
		return ResponseEntity.ok(service.findByCep(cep));
	}
}