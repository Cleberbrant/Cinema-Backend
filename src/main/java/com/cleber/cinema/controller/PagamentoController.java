package com.cleber.cinema.controller;

import com.cleber.cinema.model.Pagamento;
import com.cleber.cinema.services.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos")
public class PagamentoController {

	private final PagamentoService service;

	@Autowired
	public PagamentoController(PagamentoService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(summary = "Criar pagamento")
	public ResponseEntity<Pagamento> create(@RequestBody Pagamento pagamento) {
		return new ResponseEntity<>(service.save(pagamento), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Listar todos")
	public ResponseEntity<List<Pagamento>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID")
	public ResponseEntity<Pagamento> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar pagamento")
	public ResponseEntity<Pagamento> update(@PathVariable Integer id, @RequestBody Pagamento pagamento) {
		return ResponseEntity.ok(service.update(id, pagamento));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir pagamento")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/filme/{filmeId}")
	@Operation(summary = "Buscar por filme")
	public ResponseEntity<List<Pagamento>> findByFilme(@PathVariable Integer filmeId) {
		return ResponseEntity.ok(service.findByFilme(filmeId));
	}

	@PostMapping("/{id}/finalizar")
	@Operation(summary = "Finalizar pedido")
	public ResponseEntity<Boolean> finalizarPedido(@PathVariable Integer id) {
		Pagamento pagamento = service.findById(id);
		boolean resultado = service.finalizarPedido(pagamento);
		return ResponseEntity.ok(resultado);
	}

	@GetMapping("/{id}/total")
	@Operation(summary = "Calcular total")
	public ResponseEntity<Double> calcularTotal(@PathVariable Integer id) {
		Pagamento pagamento = service.findById(id);
		Double total = service.calcularTotal(pagamento);
		return ResponseEntity.ok(total);
	}
}
