package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.services.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {

	private final CinemaRepository cinemaRepository;

	private CinemaDTO toDTO(Cinema cinema) {
		CinemaDTO dto = new CinemaDTO();
		dto.setId(cinema.getId());
		dto.setNome(cinema.getNome());
		dto.setLocalidade(toLocalidadeDTO(cinema.getLocalidade()));
		return dto;
	}

	private LocalidadeDTO toLocalidadeDTO(Localidade localidade) {
		LocalidadeDTO dto = new LocalidadeDTO();
		dto.setId(localidade.getId());
		dto.setEndereco(localidade.getEndereco());
		dto.setCep(localidade.getCep());
		dto.setReferencia(localidade.getReferencia());
		return dto;
	}

	private Localidade toLocalidadeEntity(LocalidadeDTO dto) {
		Localidade localidade = new Localidade();
		localidade.setEndereco(dto.getEndereco());
		localidade.setCep(dto.getCep());
		localidade.setReferencia(dto.getReferencia());
		return localidade;
	}

	public CinemaDTO create(CinemaCreateDTO dto) {
		Cinema cinema = Cinema.builder()
				.nome(dto.getNome())
				.localidade(toLocalidadeEntity(dto.getLocalidade()))
				.build();
		Cinema cinemaSalvo = cinemaRepository.save(cinema);
		return toDTO(cinemaSalvo);
	}

	@Override
	public List<CinemaDTO> findAll() {
		return cinemaRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public CinemaDTO findById(Integer id) {
		Cinema cinema = cinemaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cinema não encontrado com id: " + id));
		return toDTO(cinema);
	}

	@Override
	public CinemaDTO update(Integer id, CinemaCreateDTO dto) {
		Cinema cinema = cinemaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cinema não encontrado com id: " + id));
		cinema.setNome(dto.getNome());
		cinema.setLocalidade(toLocalidadeEntity(dto.getLocalidade()));
		return toDTO(cinemaRepository.save(cinema));
	}

	@Override
	public void delete(Integer id) {
		if (!cinemaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cinema não encontrado com id: " + id);
		}
		cinemaRepository.deleteById(id);
	}

	@Override
	public List<CinemaDTO> findByNome(String nome) {
		return cinemaRepository.findByNomeContainingIgnoreCase(nome).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}