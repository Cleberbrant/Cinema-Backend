package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.repositories.AlimentoRepository;
import com.cleber.cinema.services.AlimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlimentoServiceImpl implements AlimentoService {

	private final AlimentoRepository alimentoRepository;

	@Override
	public Alimento save(Alimento alimento) {
		return alimentoRepository.save(alimento);
	}

	@Override
	public List<Alimento> findAll() {
		return alimentoRepository.findAll();
	}

	@Override
	public Alimento findById(Integer id) {
		return alimentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		alimentoRepository.deleteById(id);
	}

	@Override
	public Alimento update(Integer id, Alimento alimentoAtualizado) {
		return alimentoRepository.findById(id)
				.map(alimento -> {
					alimento.setNome(alimentoAtualizado.getNome());
					alimento.setCombo(alimentoAtualizado.getCombo());
					alimento.setPipoca(alimentoAtualizado.getPipoca());
					alimento.setBebida(alimentoAtualizado.getBebida());
					alimento.setDoces(alimentoAtualizado.getDoces());
					alimento.setPreco(alimentoAtualizado.getPreco());
					alimento.setDescricao(alimentoAtualizado.getDescricao());
					alimento.setFilme(alimentoAtualizado.getFilme());
					return alimentoRepository.save(alimento);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com id: " + id));
	}

	@Override
	public List<Alimento> findByCombo(String combo) {
		return alimentoRepository.findByCombo(combo);
	}

	@Override
	public List<Alimento> findByPrecoMaximo(Double preco) {
		return alimentoRepository.findByPrecoLessThanEqual(preco);
	}

	@Override
	public boolean selecionarLanche(Integer alimentoId) {
		return alimentoRepository.existsById(alimentoId);
	}

	@Override
	public void escolherItem(Integer alimentoId) {
		// Implemente a lógica de escolher item
	}

	@Override
	public void prosseguir() {
		// Implemente a lógica de prosseguir
	}
}
