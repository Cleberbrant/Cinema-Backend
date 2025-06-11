package com.cleber.cinema.controller;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.services.CinemaService;
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
class CinemaControllerTest {

	@Mock
	private CinemaService service;

	@InjectMocks
	private CinemaController controller;

	private CinemaDTO cinemaDTO;
	private CinemaCreateDTO createDTO;
	private LocalidadeDTO localidadeDTO;

	@BeforeEach
	void setUp() {
		localidadeDTO = new LocalidadeDTO();
		localidadeDTO.setId(1);
		localidadeDTO.setEndereco("Rua Teste, 123");
		localidadeDTO.setCep("12345-678");
		localidadeDTO.setReferencia("Próximo ao shopping");

		createDTO = new CinemaCreateDTO();
		createDTO.setNome("CineArt");
		createDTO.setLocalidade(localidadeDTO);

		cinemaDTO = new CinemaDTO();
		cinemaDTO.setId(1);
		cinemaDTO.setNome("CineArt");
		cinemaDTO.setLocalidade(localidadeDTO);
	}

	@Test
	void create_DeveRetornarCinemaCriadoComStatus201() {
		when(service.create(any(CinemaCreateDTO.class))).thenReturn(cinemaDTO);

		ResponseEntity<CinemaDTO> response = controller.create(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("CineArt", response.getBody().getNome());
		assertEquals("Rua Teste, 123", response.getBody().getLocalidade().getEndereco());
		verify(service, times(1)).create(createDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<CinemaDTO> lista = Arrays.asList(cinemaDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<CinemaDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals("CineArt", response.getBody().get(0).getNome());
		assertEquals(1, response.getBody().get(0).getLocalidade().getId());
	}

	@Test
	void findById_DeveRetornarCinemaExistente() {
		when(service.findById(1)).thenReturn(cinemaDTO);

		ResponseEntity<CinemaDTO> response = controller.findById(1);

		assertEquals(1, response.getBody().getId());
		assertEquals("CineArt", response.getBody().getNome());
		assertEquals("12345-678", response.getBody().getLocalidade().getCep());
	}

	@Test
	void update_DeveAtualizarCinemaComSucesso() {
		when(service.update(eq(1), any(CinemaCreateDTO.class))).thenReturn(cinemaDTO);

		ResponseEntity<CinemaDTO> response = controller.update(1, createDTO);

		assertEquals("CineArt", response.getBody().getNome());
		assertEquals("Próximo ao shopping", response.getBody().getLocalidade().getReferencia());
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
	void findByNome_DeveRetornarCinemasCorretos() {
		List<CinemaDTO> lista = Arrays.asList(cinemaDTO);
		when(service.findByNome("CineArt")).thenReturn(lista);

		ResponseEntity<List<CinemaDTO>> response = controller.findByNome("CineArt");

		assertEquals(1, response.getBody().size());
		assertTrue(response.getBody().get(0).getNome().contains("CineArt"));
	}
}
