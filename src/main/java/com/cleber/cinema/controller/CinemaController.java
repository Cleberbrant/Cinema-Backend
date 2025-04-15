package com.cleber.cinema.controller;

import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.services.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@Tag(name = "Cinemas", description = "Gerenciamento de cinemas")
public class CinemaController {

	private final CinemaService service;

	@Autowired
	public CinemaController(CinemaService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar cinema")
	public ResponseEntity<Cinema> create(@RequestBody Cinema cinema) {
		return new ResponseEntity<>(service.save(cinema), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<Cinema>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Cinema> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar cinema")
	public ResponseEntity<Cinema> update(@PathVariable Integer id, @RequestBody Cinema cinema) {
		return ResponseEntity.ok(service.update(id, cinema));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir cinema")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/nome/{nome}")
	@Operation(summary = "Buscar por nome")
	public ResponseEntity<List<Cinema>> findByNome(@PathVariable String nome) {
		return ResponseEntity.ok(service.findByNome(nome));
	}
}
