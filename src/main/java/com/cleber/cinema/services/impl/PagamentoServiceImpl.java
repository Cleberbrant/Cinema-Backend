package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Pagamento;
import com.cleber.cinema.repositories.PagamentoRepository;
import com.cleber.cinema.services.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoServiceImpl implements PagamentoService {

	private final PagamentoRepository pagamentoRepository;

	@Override
	public Pagamento save(Pagamento pagamento) {
		return pagamentoRepository.save(pagamento);
	}

	@Override
	public List<Pagamento> findAll() {
		return pagamentoRepository.findAll();
	}

	@Override
	public Pagamento findById(Integer id) {
		return pagamentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		pagamentoRepository.deleteById(id);
	}

	@Override
	public Pagamento update(Integer id, Pagamento pagamentoAtualizado) {
		return pagamentoRepository.findById(id)
				.map(pagamento -> {
					pagamento.setNumeroDoCartao(pagamentoAtualizado.getNumeroDoCartao());
					pagamento.setNomeImpresso(pagamentoAtualizado.getNomeImpresso());
					pagamento.setDataDeValidade(pagamentoAtualizado.getDataDeValidade());
					pagamento.setCodigoDeSeguranca(pagamentoAtualizado.getCodigoDeSeguranca());
					pagamento.setCpf(pagamentoAtualizado.getCpf());
					pagamento.setPix(pagamentoAtualizado.getPix());
					pagamento.setDinheiro(pagamentoAtualizado.getDinheiro());
					pagamento.setFilme(pagamentoAtualizado.getFilme());
					return pagamentoRepository.save(pagamento);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com id: " + id));
	}

	@Override
	public List<Pagamento> findByFilme(Integer filmeId) {
		return pagamentoRepository.findByFilmeId(filmeId);
	}

	@Override
	public boolean validacaoDaEscolha(Pagamento pagamento) {
		// Validação de cartão de crédito
		if (pagamento.getNumeroDoCartao() != null && !pagamento.getNumeroDoCartao().isEmpty()) {
			// Validações do cartão
			if (pagamento.getNomeImpresso() == null || pagamento.getNomeImpresso().isEmpty()) {
				return false;
			}
			if (pagamento.getCodigoDeSeguranca() == null || pagamento.getCodigoDeSeguranca().isEmpty()) {
				return false;
			}
			if (pagamento.getDataDeValidade() == null || pagamento.getDataDeValidade().isBefore(LocalDate.now())) {
				return false;
			}
		}

		// Validação de PIX
		if (pagamento.getPix() != null && !pagamento.getPix().isEmpty()) {
			// Validações do PIX
			if (pagamento.getCpf() == null || pagamento.getCpf().isEmpty()) {
				return false;
			}
		}

		// Validação de dinheiro
		if (pagamento.getDinheiro() != null && pagamento.getDinheiro() > 0) {
			// Validações do pagamento em dinheiro
		}

		return true;
	}

	@Override
	public boolean finalizarPedido(Pagamento pagamento) {
		if (!validacaoDaEscolha(pagamento)) {
			return false;
		}

		pagamentoRepository.save(pagamento);
		return true;
	}

	@Override
	public Double calcularTotal(Pagamento pagamento) {
		// Implementação do cálculo do total
		// Esta é uma implementação simplificada. Na prática, você precisa calcular
		// baseado nos ingressos, alimentos, etc.
		Double valorIngressos = 30.0; // Valor fixo para exemplo

		return valorIngressos;
	}
}
