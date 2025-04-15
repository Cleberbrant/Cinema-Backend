package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.repositories.LocalidadeRepository;
import com.cleber.cinema.services.LocalidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalidadeServiceImpl implements LocalidadeService {

	private final LocalidadeRepository localidadeRepository;

	@Override
	public Localidade save(Localidade localidade) {
		return localidadeRepository.save(localidade);
	}

	@Override
	public List<Localidade> findAll() {
		return localidadeRepository.findAll();
	}

	@Override
	public Localidade findById(Integer id) {
		return localidadeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Localidade não encontrada com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		if (!localidadeRepository.existsById(id)) {
			throw new ResourceNotFoundException("Localidade não encontrada com id: " + id);
		}
		localidadeRepository.deleteById(id);
	}

	@Override
	public Localidade update(Integer id, Localidade localidadeAtualizada) {
		return localidadeRepository.findById(id)
				.map(localidade -> {
					localidade.setEndereco(localidadeAtualizada.getEndereco());
					localidade.setCep(localidadeAtualizada.getCep());
					localidade.setReferencia(localidadeAtualizada.getReferencia());
					return localidadeRepository.save(localidade);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Localidade não encontrada com id: " + id));
	}

	@Override
	public List<Localidade> findByCep(String cep) {
		return localidadeRepository.findByCepStartingWith(cep);
	}
}
