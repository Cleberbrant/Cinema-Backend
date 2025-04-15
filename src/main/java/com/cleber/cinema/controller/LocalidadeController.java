package com.cleber.cinema.controller;

import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.services.LocalidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@Tag(name = "Localidades", description = "Gerenciamento de localidades")
public class LocalidadeController {

	private final LocalidadeService service;

	@Autowired
	public LocalidadeController(LocalidadeService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar localidade")
	public ResponseEntity<Localidade> create(@RequestBody Localidade localidade) {
		return new ResponseEntity<>(service.save(localidade), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<Localidade>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Localidade> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar localidade")
	public ResponseEntity<Localidade> update(@PathVariable Integer id, @RequestBody Localidade localidade) {
		return ResponseEntity.ok(service.update(id, localidade));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir localidade")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cep/{cep}")
	@Operation(summary = "Buscar por CEP")
	public ResponseEntity<List<Localidade>> findByCep(@PathVariable String cep) {
		return ResponseEntity.ok(service.findByCep(cep));
	}
}
