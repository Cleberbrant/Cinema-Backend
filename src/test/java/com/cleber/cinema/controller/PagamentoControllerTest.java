package com.cleber.cinema.controller;

import com.cleber.cinema.dto.PagamentoCreateDTO;
import com.cleber.cinema.dto.PagamentoDTO;
import com.cleber.cinema.services.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoControllerTest {

	@Mock
	private PagamentoService service;

	@InjectMocks
	private PagamentoController controller;

	private PagamentoDTO pagamentoDTO;
	private PagamentoCreateDTO createDTO;

	@BeforeEach
	void setUp() {
		createDTO = new PagamentoCreateDTO();
		createDTO.setNumeroDoCartao("1234567812345678");
		createDTO.setNomeImpresso("João Silva");
		createDTO.setDataDeValidade("12/30");
		createDTO.setCodigoDeSeguranca("123");
		createDTO.setValorTotal(89.90);
		createDTO.setFilmeId(1);
		createDTO.setAlimentosIds(Arrays.asList(1, 2));

		pagamentoDTO = new PagamentoDTO();
		pagamentoDTO.setId(1);
		pagamentoDTO.setNumeroDoCartao("123456******5678");
		pagamentoDTO.setNomeImpresso("João Silva");
		pagamentoDTO.setDataDeValidade("12/30");
		pagamentoDTO.setCodigoDeSeguranca("***");
		pagamentoDTO.setValorTotal(89.90);
		pagamentoDTO.setDataPagamento(LocalDateTime.parse("2025-06-10T20:00:00"));
		pagamentoDTO.setUsuarioId("user-123");
		pagamentoDTO.setUsuarioNome("João Silva");
		pagamentoDTO.setFilmeId(1);
		pagamentoDTO.setFilmeTitulo("Interestelar");
		pagamentoDTO.setAlimentosIds(Arrays.asList(1, 2));
		pagamentoDTO.setAlimentosNomes(Arrays.asList("Pipoca", "Refrigerante"));
	}

	@Test
	void create_DeveRetornarPagamentoCriadoComStatus201() {
		when(service.create(any(PagamentoCreateDTO.class))).thenReturn(pagamentoDTO);

		ResponseEntity<PagamentoDTO> response = controller.create(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Interestelar", response.getBody().getFilmeTitulo());
		assertEquals(2, response.getBody().getAlimentosNomes().size());
		assertEquals("user-123", response.getBody().getUsuarioId());
		verify(service, times(1)).create(createDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<PagamentoDTO> lista = Arrays.asList(pagamentoDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<PagamentoDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals(LocalDateTime.parse("2025-06-10T20:00:00"), response.getBody().get(0).getDataPagamento());
		assertEquals("João Silva", response.getBody().get(0).getUsuarioNome());
	}

	@Test
	void findById_DeveRetornarPagamentoExistente() {
		when(service.findById(1)).thenReturn(pagamentoDTO);

		ResponseEntity<PagamentoDTO> response = controller.findById(1);

		assertEquals(1, response.getBody().getId());
		assertEquals("123456******5678", response.getBody().getNumeroDoCartao());
		assertEquals("***", response.getBody().getCodigoDeSeguranca());
	}

	@Test
	void update_DeveAtualizarPagamentoComSucesso() {
		when(service.update(eq(1), any(PagamentoCreateDTO.class))).thenReturn(pagamentoDTO);

		ResponseEntity<PagamentoDTO> response = controller.update(1, createDTO);

		assertEquals("Interestelar", response.getBody().getFilmeTitulo());
		assertEquals(89.90, response.getBody().getValorTotal());
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
	void findByFilme_DeveRetornarListaFiltrada() {
		List<PagamentoDTO> lista = Arrays.asList(pagamentoDTO);
		when(service.findByFilme(1)).thenReturn(lista);

		ResponseEntity<List<PagamentoDTO>> response = controller.findByFilme(1);

		assertEquals(1, response.getBody().size());
		assertEquals("Interestelar", response.getBody().get(0).getFilmeTitulo());
		assertEquals(1, response.getBody().get(0).getFilmeId());
	}
}
