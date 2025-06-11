package com.cleber.cinema.controller;

import com.cleber.cinema.dto.SalaCreateDTO;
import com.cleber.cinema.dto.SalaDTO;
import com.cleber.cinema.services.SalaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaControllerTest {

	@Mock
	private SalaService service;

	@InjectMocks
	private SalaController controller;

	private SalaDTO salaDTO;
	private SalaCreateDTO createDTO;

	@BeforeEach
	void setUp() {
		createDTO = new SalaCreateDTO();
		createDTO.setNumeroDaSala(4);
		createDTO.setCapacidade(250);
		createDTO.setMapaDaSala("A1,A2,A3,B1,B2,B3");
		createDTO.setTecnologia("IMAX Laser");
		createDTO.setCinemaId(1);

		salaDTO = new SalaDTO();
		salaDTO.setId(1);
		salaDTO.setNumeroDaSala(4);
		salaDTO.setCapacidade(250);
		salaDTO.setMapaDaSala("A1,A2,A3,B1,B2,B3");
		salaDTO.setTecnologia("IMAX Laser");
		salaDTO.setCinemaId(1);
	}

	@Test
	void create_DeveRetornarSalaCriadaComStatus201() {
		when(service.create(any(SalaCreateDTO.class))).thenReturn(salaDTO);

		ResponseEntity<SalaDTO> response = controller.create(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("IMAX Laser", response.getBody().getTecnologia());
		assertEquals(4, response.getBody().getNumeroDaSala());
		assertEquals("A1,A2,A3,B1,B2,B3", response.getBody().getMapaDaSala());
		verify(service, times(1)).create(createDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<SalaDTO> lista = Arrays.asList(salaDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<SalaDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals(250, response.getBody().get(0).getCapacidade());
		assertEquals(4, response.getBody().get(0).getNumeroDaSala());
		assertEquals(1, response.getBody().get(0).getCinemaId());
	}

	@Test
	void findById_DeveRetornarSalaExistente() {
		when(service.findById(1)).thenReturn(salaDTO);

		ResponseEntity<SalaDTO> response = controller.findById(1);

		assertEquals(1, response.getBody().getId());
		assertEquals(4, response.getBody().getNumeroDaSala());
		assertEquals("IMAX Laser", response.getBody().getTecnologia());
		assertEquals("A1,A2,A3,B1,B2,B3", response.getBody().getMapaDaSala());
	}

	@Test
	void update_DeveAtualizarSalaComSucesso() {
		when(service.update(eq(1), any(SalaCreateDTO.class))).thenReturn(salaDTO);

		ResponseEntity<SalaDTO> response = controller.update(1, createDTO);

		assertEquals(1, response.getBody().getCinemaId());
		assertEquals("A1,A2,A3,B1,B2,B3", response.getBody().getMapaDaSala());
		assertEquals(4, response.getBody().getNumeroDaSala());
		verify(service, times(1)).update(1, createDTO);
	}

	@Test
	void delete_DeveRetornarNoContent() {
		doNothing().when(service).delete(1);

		ResponseEntity<Void> response = controller.delete(1);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(service, times(1)).delete(1);
	}

	@Test
	void findByCinema_DeveRetornarListaFiltrada() {
		List<SalaDTO> lista = Arrays.asList(salaDTO);
		when(service.findByCinema(1)).thenReturn(lista);

		ResponseEntity<List<SalaDTO>> response = controller.findByCinema(1);

		assertEquals(1, response.getBody().size());
		assertEquals(1, response.getBody().get(0).getCinemaId());
		assertEquals("IMAX Laser", response.getBody().get(0).getTecnologia());
	}

	@Test
	void findByTecnologia_DeveRetornarListaFiltrada() {
		List<SalaDTO> lista = Arrays.asList(salaDTO);
		when(service.findByTecnologia("IMAX Laser")).thenReturn(lista);

		ResponseEntity<List<SalaDTO>> response = controller.findByTecnologia("IMAX Laser");

		assertEquals(1, response.getBody().size());
		assertEquals("IMAX Laser", response.getBody().get(0).getTecnologia());
		assertEquals(4, response.getBody().get(0).getNumeroDaSala());
	}
}
