package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.SalaCreateDTO;
import com.cleber.cinema.dto.SalaDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Sala;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.repositories.SalaRepository;
import com.cleber.cinema.services.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaServiceImpl implements SalaService {

	private final SalaRepository salaRepository;
	private final CinemaRepository cinemaRepository;

	private SalaDTO toDTO(Sala sala) {
		SalaDTO dto = new SalaDTO();
		dto.setId(sala.getId());
		dto.setNumeroDaSala(sala.getNumeroDaSala());
		dto.setCapacidade(sala.getCapacidade());
		dto.setMapaDaSala(sala.getMapaDaSala());
		dto.setTecnologia(sala.getTecnologia());
		dto.setCinemaId(sala.getCinema().getId());

		return dto;
	}

	private Sala toEntity(SalaCreateDTO dto) {
		Cinema cinema = cinemaRepository.findById(dto.getCinemaId())
				.orElseThrow(() -> new ResourceNotFoundException("Cinema não encontrado com id: " + dto.getCinemaId()));
		return Sala.builder()
				.numeroDaSala(dto.getNumeroDaSala())
				.capacidade(dto.getCapacidade())
				.mapaDaSala(dto.getMapaDaSala())
				.tecnologia(dto.getTecnologia())
				.cinema(cinema)
				.build();
	}

	@Override
	public SalaDTO create(SalaCreateDTO dto) {
		Sala sala = toEntity(dto);
		return toDTO(salaRepository.save(sala));
	}

	@Override
	public List<SalaDTO> findAll() {
		return salaRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public SalaDTO findById(Integer id) {
		Sala sala = salaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com id: " + id));
		return toDTO(sala);
	}

	@Override
	public SalaDTO update(Integer id, SalaCreateDTO dto) {
		Sala sala = salaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com id: " + id));
		Cinema cinema = cinemaRepository.findById(dto.getCinemaId())
				.orElseThrow(() -> new ResourceNotFoundException("Cinema não encontrado com id: " + dto.getCinemaId()));
		sala.setNumeroDaSala(dto.getNumeroDaSala());
		sala.setCapacidade(dto.getCapacidade());
		sala.setMapaDaSala(dto.getMapaDaSala());
		sala.setTecnologia(dto.getTecnologia());
		sala.setCinema(cinema);
		return toDTO(salaRepository.save(sala));
	}

	@Override
	public void delete(Integer id) {
		if (!salaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Sala não encontrada com id: " + id);
		}
		salaRepository.deleteById(id);
	}

	@Override
	public List<SalaDTO> findByCinema(Integer cinemaId) {
		return salaRepository.findByCinemaId(cinemaId).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalaDTO> findByTecnologia(String tecnologia) {
		return salaRepository.findByTecnologia(tecnologia).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}