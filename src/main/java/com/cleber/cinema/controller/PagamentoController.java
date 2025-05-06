package com.cleber.cinema.controller;

import com.cleber.cinema.dto.PagamentoCreateDTO;
import com.cleber.cinema.dto.PagamentoDTO;
import com.cleber.cinema.services.PagamentoService;
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
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

	private final PagamentoService service;

	@PostMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Criar pagamento")
	public ResponseEntity<PagamentoDTO> create(@Valid @RequestBody PagamentoCreateDTO dto) {
		PagamentoDTO created = service.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<PagamentoDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<PagamentoDTO> findById(@PathVariable Integer id) {
		PagamentoDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Atualizar pagamento")
	public ResponseEntity<PagamentoDTO> update(@PathVariable Integer id, @Valid @RequestBody PagamentoCreateDTO dto) {
		PagamentoDTO updated = service.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Excluir pagamento")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/filme/{filmeId}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Buscar por filme")
	public ResponseEntity<List<PagamentoDTO>> findByFilme(@PathVariable Integer filmeId) {
		return ResponseEntity.ok(service.findByFilme(filmeId));
	}
}