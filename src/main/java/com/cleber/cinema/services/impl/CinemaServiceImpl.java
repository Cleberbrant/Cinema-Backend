package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.services.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {

	private final CinemaRepository cinemaRepository;

	@Override
	public Cinema save(Cinema cinema) {
		return cinemaRepository.save(cinema);
	}

	@Override
	public List<Cinema> findAll() {
		return cinemaRepository.findAll();
	}

	@Override
	public Cinema findById(Integer id) {
		return cinemaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cinema não encontrado com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		if (!cinemaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cinema não encontrado com id: " + id);
		}
		cinemaRepository.deleteById(id);
	}

	@Override
	public Cinema update(Integer id, Cinema cinemaAtualizado) {
		return cinemaRepository.findById(id)
				.map(cinema -> {
					cinema.setNome(cinemaAtualizado.getNome());
					if (cinemaAtualizado.getLocalidade() != null) {
						cinema.setLocalidade(cinemaAtualizado.getLocalidade());
					}
					return cinemaRepository.save(cinema);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Cinema não encontrado com id: " + id));
	}

	@Override
	public List<Cinema> findByNome(String nome) {
		return cinemaRepository.findByNomeContainingIgnoreCase(nome);
	}
}
