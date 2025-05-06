package com.cleber.cinema.controller;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.services.LocalidadeService;
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
@RequestMapping("/api/localidades")
@Tag(name = "Localidades", description = "Gerenciamento de localidades")
@RequiredArgsConstructor
public class LocalidadeController {

	private final LocalidadeService service;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar localidade")
	public ResponseEntity<LocalidadeDTO> create(@Valid @RequestBody LocalidadeCreateDTO dto) {
		LocalidadeDTO created = service.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<LocalidadeDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<LocalidadeDTO> findById(@PathVariable Integer id) {
		LocalidadeDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar localidade")
	public ResponseEntity<LocalidadeDTO> update(@PathVariable Integer id, @Valid @RequestBody LocalidadeCreateDTO dto) {
		LocalidadeDTO updated = service.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir localidade")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cep/{cep}")
	@Operation(summary = "Buscar por CEP")
	public ResponseEntity<List<LocalidadeDTO>> findByCep(@PathVariable String cep) {
		return ResponseEntity.ok(service.findByCep(cep));
	}
}