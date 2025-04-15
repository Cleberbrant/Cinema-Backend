package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Sala;
import com.cleber.cinema.repositories.SalaRepository;
import com.cleber.cinema.services.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaServiceImpl implements SalaService {

	private final SalaRepository salaRepository;

	@Override
	public Sala save(Sala sala) {
		return salaRepository.save(sala);
	}

	@Override
	public List<Sala> findAll() {
		return salaRepository.findAll();
	}

	@Override
	public Sala findById(Integer id) {
		return salaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		if (!salaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Sala não encontrada com id: " + id);
		}
		salaRepository.deleteById(id);
	}

	@Override
	public Sala update(Integer id, Sala salaAtualizada) {
		return salaRepository.findById(id)
				.map(sala -> {
					sala.setNumeroDaSala(salaAtualizada.getNumeroDaSala());
					sala.setCapacidade(salaAtualizada.getCapacidade());
					sala.setMapaDaSala(salaAtualizada.getMapaDaSala());
					sala.setTecnologia(salaAtualizada.getTecnologia());
					if (salaAtualizada.getCinema() != null) {
						sala.setCinema(salaAtualizada.getCinema());
					}
					return salaRepository.save(sala);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com id: " + id));
	}

	@Override
	public List<Sala> findByCinema(Integer cinemaId) {
		return salaRepository.findByCinemaId(cinemaId);
	}

	@Override
	public List<Sala> findByTecnologia(String tecnologia) {
		return salaRepository.findByTecnologia(tecnologia);
	}

	@Override
	public boolean selecionarSala(Integer salaId) {
		return salaRepository.existsById(salaId);
	}

	@Override
	public boolean selecionarTecnologia(String tecnologia) {
		return !salaRepository.findByTecnologia(tecnologia).isEmpty();
	}

	@Override
	public boolean confirmacaoSala(Sala sala) {
		// Implementação da lógica de confirmação de sala
		if (sala.getNumeroDaSala() == null || sala.getNumeroDaSala() <= 0) {
			return false;
		}
		if (sala.getCapacidade() == null || sala.getCapacidade() <= 0) {
			return false;
		}
		if (sala.getCinema() == null) {
			return false;
		}
		return true;
	}

	@Override
	public void criarSala(Sala sala) {
		if (!confirmacaoSala(sala)) {
			throw new IllegalArgumentException("Dados inválidos para criação de sala");
		}
		salaRepository.save(sala);
	}
}
