package com.cleber.cinema.controller;

import com.cleber.cinema.dto.ClienteDTO;
import com.cleber.cinema.model.Cliente;
import com.cleber.cinema.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {

	private final ClienteService service;

	@Autowired
	public ClienteController(ClienteService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar um novo cliente",
			description = "Cria um novo cliente com os dados fornecidos")
	@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	public ResponseEntity<Cliente> create(@Valid @RequestBody ClienteDTO clienteDTO) {
		Cliente novoCliente = service.createFromDTO(clienteDTO);
		return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos os clientes",
			description = "Retorna uma lista com todos os clientes cadastrados")
	@ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
	public ResponseEntity<List<Cliente>> findAll() {
		List<Cliente> clientes = service.findAll();
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar cliente por ID",
			description = "Retorna um cliente com base no ID fornecido")
	@ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	public ResponseEntity<Cliente> findById(@PathVariable Integer id) {
		return service.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar cliente",
			description = "Atualiza os dados de um cliente existente")
	@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	public ResponseEntity<Cliente> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO clienteDTO) {
		Cliente clienteAtualizado = service.updateFromDTO(id, clienteDTO);
		return ResponseEntity.ok(clienteAtualizado);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir cliente",
			description = "Remove um cliente com base no ID fornecido")
	@ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso")
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cpf/{cpf}")
	@Operation(summary = "Buscar cliente por CPF",
			description = "Retorna um cliente com base no CPF fornecido")
	@ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	public ResponseEntity<Cliente> findByCpf(@PathVariable String cpf) {
		return service.findByCpf(cpf)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	@Operation(summary = "Buscar cliente por e-mail",
			description = "Retorna um cliente com base no e-mail fornecido")
	@ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	public ResponseEntity<Cliente> findByEmail(@PathVariable String email) {
		return service.findByEmail(email)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
