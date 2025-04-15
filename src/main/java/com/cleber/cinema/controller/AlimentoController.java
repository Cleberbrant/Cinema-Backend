package com.cleber.cinema.controller;

import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.services.AlimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
@Tag(name = "Alimentos", description = "Gerenciamento de alimentos/lanches")
public class AlimentoController {

	private final AlimentoService service;

	@Autowired
	public AlimentoController(AlimentoService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar alimento")
	public ResponseEntity<Alimento> create(@RequestBody Alimento alimento) {
		return new ResponseEntity<>(service.save(alimento), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<Alimento>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Alimento> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar alimento")
	public ResponseEntity<Alimento> update(@PathVariable Integer id, @RequestBody Alimento alimento) {
		return ResponseEntity.ok(service.update(id, alimento));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir alimento")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/combo/{combo}")
	@Operation(summary = "Buscar por combo")
	public ResponseEntity<List<Alimento>> findByCombo(@PathVariable String combo) {
		return ResponseEntity.ok(service.findByCombo(combo));
	}

	@GetMapping("/preco/{preco}")
	@Operation(summary = "Buscar por preço máximo")
	public ResponseEntity<List<Alimento>> findByPrecoMaximo(@PathVariable Double preco) {
		return ResponseEntity.ok(service.findByPrecoMaximo(preco));
	}
}
