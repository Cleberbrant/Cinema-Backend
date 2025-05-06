package com.cleber.cinema.controller;

import com.cleber.cinema.dto.AlimentoDTO;
import com.cleber.cinema.enums.TipoAlimento;
import com.cleber.cinema.services.AlimentoService;
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
@RequestMapping("/api/alimentos")
@Tag(name = "Alimentos", description = "Gerenciamento de alimentos/lanches")
@RequiredArgsConstructor
public class AlimentoController {

	private final AlimentoService service;

	// Criar alimento (apenas ADMIN)
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Criar alimento")
	public ResponseEntity<AlimentoDTO> create(@Valid @RequestBody AlimentoDTO dto) {
		return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
	}

	// Listar todos (aberto)
	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<AlimentoDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	// Buscar por ID (aberto)
	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<AlimentoDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	// Atualizar alimento (apenas ADMIN)
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Atualizar alimento")
	public ResponseEntity<AlimentoDTO> update(@PathVariable Integer id, @Valid @RequestBody AlimentoDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	// Excluir alimento (apenas ADMIN)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Excluir alimento")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Buscar por tipo (aberto)
	@GetMapping("/tipo/{tipo}")
	@Operation(summary = "Buscar por tipo")
	public ResponseEntity<List<AlimentoDTO>> findByTipo(@PathVariable TipoAlimento tipo) {
		return ResponseEntity.ok(service.findByTipo(tipo));
	}

	// Buscar por nome (aberto)
	@GetMapping("/nome/{nome}")
	@Operation(summary = "Buscar por nome")
	public ResponseEntity<List<AlimentoDTO>> findByNome(@PathVariable String nome) {
		return ResponseEntity.ok(service.findByNome(nome));
	}

	// Buscar por preço máximo (aberto)
	@GetMapping("/preco/{preco}")
	@Operation(summary = "Buscar por preço máximo")
	public ResponseEntity<List<AlimentoDTO>> findByPrecoMaximo(@PathVariable Double preco) {
		return ResponseEntity.ok(service.findByPrecoMaximo(preco));
	}
}