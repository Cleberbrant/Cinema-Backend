package com.cleber.cinema.services;

import com.cleber.cinema.model.Pagamento;

import java.util.List;

public interface PagamentoService {
	Pagamento save(Pagamento pagamento);
	List<Pagamento> findAll();
	Pagamento findById(Integer id);
	void delete(Integer id);
	Pagamento update(Integer id, Pagamento pagamento);
	List<Pagamento> findByFilme(Integer filmeId);
	boolean validacaoDaEscolha(Pagamento pagamento);
	boolean finalizarPedido(Pagamento pagamento);
	Double calcularTotal(Pagamento pagamento);
}
