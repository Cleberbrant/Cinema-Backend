package com.cleber.cinema.controller;

import com.cleber.cinema.model.Administrador;
import com.cleber.cinema.services.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@Tag(name = "Administradores", description = "Gerenciamento de administradores")
public class AdministradorController {

	private final AdministradorService service;

	@Autowired
	public AdministradorController(AdministradorService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar administrador")
	public ResponseEntity<Administrador> create(@RequestBody Administrador administrador) {
		return new ResponseEntity<>(service.save(administrador), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<Administrador>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Administrador> findById(@PathVariable Integer id) {
		return service.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar administrador")
	public ResponseEntity<Administrador> update(@PathVariable Integer id, @RequestBody Administrador administrador) {
		return ResponseEntity.ok(service.update(id, administrador));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir administrador")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cpf/{cpf}")
	@Operation(summary = "Buscar por CPF")
	public ResponseEntity<Administrador> findByCpf(@PathVariable String cpf) {
		return service.findByCpf(cpf)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	@Operation(summary = "Buscar por e-mail")
	public ResponseEntity<Administrador> findByEmail(@PathVariable String email) {
		return service.findByEmail(email)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
