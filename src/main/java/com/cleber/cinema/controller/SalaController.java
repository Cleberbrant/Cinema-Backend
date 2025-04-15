package com.cleber.cinema.controller;

import com.cleber.cinema.model.Sala;
import com.cleber.cinema.services.PagamentoService;
import com.cleber.cinema.services.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@Tag(name = "Salas", description = "Gerenciamento de salas de cinema")
public class SalaController {

	private final SalaService service;

	@Autowired
	public SalaController(SalaService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar sala")
	public ResponseEntity<Sala> create(@RequestBody Sala sala) {
		service.criarSala(sala);
		return new ResponseEntity<>(sala, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<Sala>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Sala> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar sala")
	public ResponseEntity<Sala> update(@PathVariable Integer id, @RequestBody Sala sala) {
		return ResponseEntity.ok(service.update(id, sala));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir sala")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cinema/{cinemaId}")
	@Operation(summary = "Buscar por cinema")
	public ResponseEntity<List<Sala>> findByCinema(@PathVariable Integer cinemaId) {
		return ResponseEntity.ok(service.findByCinema(cinemaId));
	}

	@GetMapping("/tecnologia/{tecnologia}")
	@Operation(summary = "Buscar por tecnologia")
	public ResponseEntity<List<Sala>> findByTecnologia(@PathVariable String tecnologia) {
		return ResponseEntity.ok(service.findByTecnologia(tecnologia));
	}
}
