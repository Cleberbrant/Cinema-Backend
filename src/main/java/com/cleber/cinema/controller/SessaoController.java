package com.cleber.cinema.controller;

import com.cleber.cinema.model.Sessao;
import com.cleber.cinema.services.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessoes")
@Tag(name = "Sessões", description = "Gerenciamento de sessões de cinema")
public class SessaoController {

	private final SessaoService service;

	@Autowired
	public SessaoController(SessaoService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar sessão")
	public ResponseEntity<Sessao> create(@RequestBody Sessao sessao) {
		return new ResponseEntity<>(service.save(sessao), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todas")
	public ResponseEntity<List<Sessao>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Sessao> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar sessão")
	public ResponseEntity<Sessao> update(@PathVariable Integer id, @RequestBody Sessao sessao) {
		return ResponseEntity.ok(service.update(id, sessao));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir sessão")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/filme/{filmeId}")
	@Operation(summary = "Buscar por filme")
	public ResponseEntity<List<Sessao>> findByFilme(@PathVariable Integer filmeId) {
		return ResponseEntity.ok(service.findByFilme(filmeId));
	}

	@GetMapping("/sala/{salaId}")
	@Operation(summary = "Buscar por sala")
	public ResponseEntity<List<Sessao>> findBySala(@PathVariable Integer salaId) {
		return ResponseEntity.ok(service.findBySala(salaId));
	}

	@GetMapping("/data/{data}")
	@Operation(summary = "Buscar por data")
	public ResponseEntity<List<Sessao>> findByData(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
		return ResponseEntity.ok(service.findByData(data));
	}
}
