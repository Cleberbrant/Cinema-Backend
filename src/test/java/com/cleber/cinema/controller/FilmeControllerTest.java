package com.cleber.cinema.controller;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.services.FilmeService;
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
class FilmeControllerTest {

	@Mock
	private FilmeService service;

	@InjectMocks
	private FilmeController controller;

	private FilmeDTO filmeDTO;

	@BeforeEach
	void setUp() {
		filmeDTO = new FilmeDTO();
		filmeDTO.setId(1);
		filmeDTO.setTitulo("Interestelar");
		filmeDTO.setGenero(GeneroFilme.FICCAO_CIENTIFICA);
		filmeDTO.setEmCartaz(true);
	}

	@Test
	void create_DeveRetornarFilmeCriadoComStatus201() {
		when(service.create(any(FilmeDTO.class))).thenReturn(filmeDTO);

		ResponseEntity<FilmeDTO> response = controller.create(filmeDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Interestelar", response.getBody().getTitulo());
		verify(service, times(1)).create(filmeDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<FilmeDTO> lista = Arrays.asList(filmeDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<FilmeDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals("Interestelar", response.getBody().get(0).getTitulo());
	}

	@Test
	void findById_DeveRetornarFilmeExistente() {
		when(service.findById(1)).thenReturn(filmeDTO);

		ResponseEntity<FilmeDTO> response = controller.findById(1);

		assertEquals(1, response.getBody().getId());
		assertEquals(GeneroFilme.FICCAO_CIENTIFICA, response.getBody().getGenero());
	}

	@Test
	void update_DeveAtualizarFilmeComSucesso() {
		when(service.update(eq(1), any(FilmeDTO.class))).thenReturn(filmeDTO);

		ResponseEntity<FilmeDTO> response = controller.update(1, filmeDTO);

		assertEquals("Interestelar", response.getBody().getTitulo());
		verify(service, times(1)).update(1, filmeDTO);
	}

	@Test
	void delete_DeveRetornarNoContent() {
		doNothing().when(service).delete(1);

		ResponseEntity<Void> response = controller.delete(1);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(service, times(1)).delete(1);
	}

	@Test
	void findByEmCartaz_DeveRetornarListaFiltrada() {
		List<FilmeDTO> lista = Arrays.asList(filmeDTO);
		when(service.findByEmCartaz(true)).thenReturn(lista);

		ResponseEntity<List<FilmeDTO>> response = controller.findByEmCartaz(true);

		assertEquals(1, response.getBody().size());
		assertTrue(response.getBody().get(0).isEmCartaz());
	}

	@Test
	void findByGenero_DeveRetornarListaPorGenero() {
		List<FilmeDTO> lista = Arrays.asList(filmeDTO);
		when(service.findByGenero(GeneroFilme.FICCAO_CIENTIFICA)).thenReturn(lista);

		ResponseEntity<List<FilmeDTO>> response = controller.findByGenero(GeneroFilme.FICCAO_CIENTIFICA);

		assertEquals(1, response.getBody().size());
		assertEquals(GeneroFilme.FICCAO_CIENTIFICA, response.getBody().get(0).getGenero());
	}

	@Test
	void findByTitulo_DeveRetornarListaPorTitulo() {
		List<FilmeDTO> lista = Arrays.asList(filmeDTO);
		when(service.findByTitulo("Interestelar")).thenReturn(lista);

		ResponseEntity<List<FilmeDTO>> response = controller.findByTitulo("Interestelar");

		assertEquals(1, response.getBody().size());
		assertEquals("Interestelar", response.getBody().get(0).getTitulo());
	}
}
