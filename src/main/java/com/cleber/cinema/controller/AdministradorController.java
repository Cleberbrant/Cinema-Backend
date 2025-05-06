package com.cleber.cinema.controller;

import com.cleber.cinema.dto.AdministradorCreateDTO;
import com.cleber.cinema.dto.AdministradorDTO;
import com.cleber.cinema.services.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administradores")
@Tag(name = "Administradores", description = "Gerenciamento de administradores")
public class AdministradorController {

	private final AdministradorService service;

	public AdministradorController(AdministradorService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar administrador")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Administrador criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos")
	})
	public ResponseEntity<AdministradorDTO> create(@RequestBody AdministradorCreateDTO dto) {
		AdministradorDTO created = service.save(dto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<AdministradorDTO>> findAll() {
		List<AdministradorDTO> list = service.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<AdministradorDTO> findById(@PathVariable Integer id) {
		Optional<AdministradorDTO> dto = service.findById(id);
		return dto.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar administrador")
	public ResponseEntity<AdministradorDTO> update(@PathVariable Integer id, @RequestBody AdministradorCreateDTO dto) {
		AdministradorDTO updated = service.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir administrador")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cpf/{cpf}")
	@Operation(summary = "Buscar por CPF")
	public ResponseEntity<AdministradorDTO> findByCpf(@PathVariable String cpf) {
		Optional<AdministradorDTO> dto = service.findByCpf(cpf);
		return dto.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	@Operation(summary = "Buscar por e-mail")
	public ResponseEntity<AdministradorDTO> findByEmail(@PathVariable String email) {
		Optional<AdministradorDTO> dto = service.findByEmail(email);
		return dto.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
