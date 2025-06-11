package com.cleber.cinema.controller;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.services.LocalidadeService;
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
class LocalidadeControllerTest {

	@Mock
	private LocalidadeService service;

	@InjectMocks
	private LocalidadeController controller;

	private LocalidadeDTO localidadeDTO;
	private LocalidadeCreateDTO createDTO;

	@BeforeEach
	void setUp() {
		createDTO = new LocalidadeCreateDTO();
		createDTO.setEndereco("Rua Teste, 123");
		createDTO.setCep("12345-678");
		createDTO.setReferencia("Próximo ao shopping");

		localidadeDTO = new LocalidadeDTO();
		localidadeDTO.setId(1);
		localidadeDTO.setEndereco("Rua Teste, 123");
		localidadeDTO.setCep("12345-678");
		localidadeDTO.setReferencia("Próximo ao shopping");
	}

	@Test
	void create_DeveRetornarLocalidadeCriadaComStatus201() {
		when(service.create(any(LocalidadeCreateDTO.class))).thenReturn(localidadeDTO);

		ResponseEntity<LocalidadeDTO> response = controller.create(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("12345-678", response.getBody().getCep());
		verify(service, times(1)).create(createDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<LocalidadeDTO> lista = Arrays.asList(localidadeDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<LocalidadeDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals("Rua Teste, 123", response.getBody().get(0).getEndereco());
	}

	@Test
	void findById_DeveRetornarLocalidadeExistente() {
		when(service.findById(1)).thenReturn(localidadeDTO);

		ResponseEntity<LocalidadeDTO> response = controller.findById(1);

		assertEquals(1, response.getBody().getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void update_DeveAtualizarLocalidadeComSucesso() {
		when(service.update(eq(1), any(LocalidadeCreateDTO.class))).thenReturn(localidadeDTO);

		ResponseEntity<LocalidadeDTO> response = controller.update(1, createDTO);

		assertEquals("Próximo ao shopping", response.getBody().getReferencia());
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
	void findByCep_DeveRetornarListaFiltrada() {
		List<LocalidadeDTO> lista = Arrays.asList(localidadeDTO);
		when(service.findByCep("12345-678")).thenReturn(lista);

		ResponseEntity<List<LocalidadeDTO>> response = controller.findByCep("12345-678");

		assertEquals(1, response.getBody().size());
		assertEquals("12345-678", response.getBody().get(0).getCep());
	}
}
