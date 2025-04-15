package com.cleber.cinema.controller;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.services.FilmeService;
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
@RequestMapping("/api/filmes")
@Tag(name = "Filmes", description = "API para gerenciamento de filmes")
public class FilmeController {

	private final FilmeService service;

	@Autowired
	public FilmeController(FilmeService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar um novo filme",
			description = "Cria um novo filme com os dados fornecidos")
	@ApiResponse(responseCode = "201", description = "Filme criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	public ResponseEntity<Filme> create(@Valid @RequestBody FilmeDTO filmeDTO) {
		Filme novoFilme = service.createFromDTO(filmeDTO);
		return new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos os filmes",
			description = "Retorna uma lista com todos os filmes cadastrados")
	@ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
	public ResponseEntity<List<Filme>> findAll() {
		List<Filme> filmes = service.findAll();
		return ResponseEntity.ok(filmes);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar filme por ID",
			description = "Retorna um filme com base no ID fornecido")
	@ApiResponse(responseCode = "200", description = "Filme encontrado com sucesso")
	@ApiResponse(responseCode = "404", description = "Filme não encontrado")
	public ResponseEntity<Filme> findById(@PathVariable Integer id) {
		try {
			Filme filme = service.findById(id);
			return ResponseEntity.ok(filme);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar filme",
			description = "Atualiza os dados de um filme existente")
	@ApiResponse(responseCode = "200", description = "Filme atualizado com sucesso")
	@ApiResponse(responseCode = "404", description = "Filme não encontrado")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	public ResponseEntity<Filme> update(@PathVariable Integer id, @Valid @RequestBody FilmeDTO filmeDTO) {
		Filme filmeAtualizado = service.updateFromDTO(id, filmeDTO);
		return ResponseEntity.ok(filmeAtualizado);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir filme",
			description = "Remove um filme com base no ID fornecido")
	@ApiResponse(responseCode = "204", description = "Filme excluído com sucesso")
	@ApiResponse(responseCode = "404", description = "Filme não encontrado")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/em-cartaz/{status}")
	@Operation(summary = "Listar filmes em cartaz",
			description = "Retorna uma lista com todos os filmes em cartaz")
	@ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
	public ResponseEntity<List<Filme>> findByEmCartaz(@PathVariable boolean status) {
		List<Filme> filmes = service.findByEmCartaz(status);
		return ResponseEntity.ok(filmes);
	}

	@GetMapping("/genero/{genero}")
	@Operation(summary = "Listar filmes por gênero",
			description = "Retorna uma lista com todos os filmes de um determinado gênero")
	@ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
	public ResponseEntity<List<Filme>> findByGenero(@PathVariable String genero) {
		List<Filme> filmes = service.findByGenero(genero);
		return ResponseEntity.ok(filmes);
	}

	@GetMapping("/titulo/{titulo}")
	@Operation(summary = "Listar filmes por título",
			description = "Retorna uma lista com todos os filmes que contêm o título fornecido")
	@ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
	public ResponseEntity<List<Filme>> findByTitulo(@PathVariable String titulo) {
		List<Filme> filmes = service.findByTitulo(titulo);
		return ResponseEntity.ok(filmes);
	}
}