package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.SalaCreateDTO;
import com.cleber.cinema.dto.SalaDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Sala;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.repositories.SalaRepository;
import com.cleber.cinema.services.impl.SalaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalaServiceImplTest {

	@Mock
	private SalaRepository salaRepository;

	@Mock
	private CinemaRepository cinemaRepository;

	@InjectMocks
	private SalaServiceImpl service;

	private Sala sala;
	private SalaCreateDTO createDTO;

	@BeforeEach
	void setUp() {
		Cinema cinema = new Cinema();
		cinema.setId(1); // Definir ID do cinema

		sala = new Sala();
		sala.setId(1);
		sala.setNumeroDaSala(4);
		sala.setCapacidade(250);
		sala.setMapaDaSala("A1,A2,A3");
		sala.setTecnologia("IMAX");
		sala.setCinema(cinema);

		createDTO = new SalaCreateDTO();
		createDTO.setNumeroDaSala(4);
		createDTO.setCapacidade(250);
		createDTO.setMapaDaSala("A1,A2,A3");
		createDTO.setTecnologia("IMAX");
		createDTO.setCinemaId(1);
	}

	@Test
	void create_DeveRetornarSalaDTO() {
		when(cinemaRepository.findById(1)).thenReturn(Optional.of(new Cinema()));
		when(salaRepository.save(any())).thenReturn(sala);
		SalaDTO result = service.create(createDTO);
		assertNotNull(result);
		assertEquals(1, result.getCinemaId());
		verify(salaRepository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDeSalas() {
		when(salaRepository.findAll()).thenReturn(List.of(sala));
		List<SalaDTO> result = service.findAll();
		assertEquals(1, result.size());
		assertEquals(1, result.getFirst().getCinemaId());
	}

	@Test
	void findById_DeveRetornarSalaDTO() {
		when(salaRepository.findById(1)).thenReturn(Optional.of(sala));
		SalaDTO result = service.findById(1);
		assertNotNull(result);
		assertEquals(1, result.getCinemaId());
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(salaRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	void update_DeveAtualizarCamposCorretamente() {
		Cinema cinema = new Cinema();
		cinema.setId(1);

		Sala salaAtualizada = new Sala();
		salaAtualizada.setCinema(cinema);

		when(salaRepository.findById(1)).thenReturn(Optional.of(sala));
		when(cinemaRepository.findById(1)).thenReturn(Optional.of(cinema));
		when(salaRepository.save(any())).thenReturn(salaAtualizada);

		SalaDTO result = service.update(1, createDTO);
		assertEquals(1, result.getCinemaId()); // Agora passará
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(salaRepository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(salaRepository).deleteById(1);
	}

	@Test
	void findByCinema_DeveRetornarListaFiltrada() {
		when(salaRepository.findByCinemaId(1)).thenReturn(List.of(sala));
		List<SalaDTO> result = service.findByCinema(1);
		assertEquals(1, result.size());
		assertEquals(1, result.getFirst().getCinemaId());
	}

	@Test
	void findByTecnologia_DeveRetornarListaFiltrada() {
		when(salaRepository.findByTecnologia("IMAX")).thenReturn(List.of(sala));
		List<SalaDTO> result = service.findByTecnologia("IMAX");
		assertEquals(1, result.size());
		assertEquals(1, result.getFirst().getCinemaId());
	}
}
