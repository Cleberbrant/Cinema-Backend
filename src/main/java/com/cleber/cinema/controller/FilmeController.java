package com.cleber.cinema.controller;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.services.FilmeService;
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
@RequestMapping("/api/filmes")
@RequiredArgsConstructor
@Tag(name = "Filmes", description = "API para gerenciamento de filmes")
public class FilmeController {

	private final FilmeService service;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar um novo filme")
	public ResponseEntity<FilmeDTO> create(@Valid @RequestBody FilmeDTO filmeDTO) {
		FilmeDTO novoFilme = service.create(filmeDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoFilme);
	}

	@GetMapping
	@Operation(summary = "Listar todos os filmes")
	public ResponseEntity<List<FilmeDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar filme por ID")
	public ResponseEntity<FilmeDTO> findById(@PathVariable Integer id) {
		FilmeDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar filme")
	public ResponseEntity<FilmeDTO> update(@PathVariable Integer id, @Valid @RequestBody FilmeDTO filmeDTO) {
		FilmeDTO updated = service.update(id, filmeDTO);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir filme")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/em-cartaz/{status}")
	@Operation(summary = "Listar filmes em cartaz")
	public ResponseEntity<List<FilmeDTO>> findByEmCartaz(@PathVariable boolean status) {
		return ResponseEntity.ok(service.findByEmCartaz(status));
	}

	@GetMapping("/genero/{genero}")
	@Operation(summary = "Listar filmes por gênero")
	public ResponseEntity<List<FilmeDTO>> findByGenero(@PathVariable GeneroFilme genero) {
		return ResponseEntity.ok(service.findByGenero(genero));
	}

	@GetMapping("/titulo/{titulo}")
	@Operation(summary = "Listar filmes por título")
	public ResponseEntity<List<FilmeDTO>> findByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(service.findByTitulo(titulo));
	}
}