package com.cleber.cinema.controller;

import com.cleber.cinema.dto.SessaoCreateDTO;
import com.cleber.cinema.dto.SessaoDTO;
import com.cleber.cinema.services.SessaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoControllerTest {

	@Mock
	private SessaoService service;

	@InjectMocks
	private SessaoController controller;

	private SessaoDTO sessaoDTO;
	private SessaoCreateDTO createDTO;
	private LocalDateTime dataHoraSessao;

	@BeforeEach
	void setUp() {
		dataHoraSessao = LocalDateTime.of(2025, 6, 11, 18, 30);

		createDTO = new SessaoCreateDTO();
		createDTO.setDataHoraSessao(dataHoraSessao);
		createDTO.setSalaId(1);
		createDTO.setFilmeId(1);

		sessaoDTO = new SessaoDTO();
		sessaoDTO.setId(1);
		sessaoDTO.setDataHoraSessao(dataHoraSessao);
		sessaoDTO.setSalaId(1);
		sessaoDTO.setFilmeId(1);
	}

	@Test
	void create_DeveRetornarSessaoCriadaComStatus201() {
		when(service.create(any(SessaoCreateDTO.class))).thenReturn(sessaoDTO);

		ResponseEntity<SessaoDTO> response = controller.create(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(dataHoraSessao, response.getBody().getDataHoraSessao());
		assertEquals(1, response.getBody().getSalaId());
		assertEquals(1, response.getBody().getFilmeId());
		verify(service, times(1)).create(createDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<SessaoDTO> lista = Arrays.asList(sessaoDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<SessaoDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals(1, response.getBody().get(0).getId());
		assertEquals(dataHoraSessao, response.getBody().get(0).getDataHoraSessao());
	}

	@Test
	void findById_DeveRetornarSessaoExistente() {
		when(service.findById(1)).thenReturn(sessaoDTO);

		ResponseEntity<SessaoDTO> response = controller.findById(1);

		assertEquals(1, response.getBody().getId());
		assertEquals(1, response.getBody().getSalaId());
		assertEquals(1, response.getBody().getFilmeId());
		assertEquals(dataHoraSessao, response.getBody().getDataHoraSessao());
	}

	@Test
	void update_DeveAtualizarSessaoComSucesso() {
		when(service.update(eq(1), any(SessaoCreateDTO.class))).thenReturn(sessaoDTO);

		ResponseEntity<SessaoDTO> response = controller.update(1, createDTO);

		assertEquals(1, response.getBody().getFilmeId());
		assertEquals(1, response.getBody().getSalaId());
		assertEquals(dataHoraSessao, response.getBody().getDataHoraSessao());
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
		List<SessaoDTO> lista = Arrays.asList(sessaoDTO);
		when(service.findByFilme(1)).thenReturn(lista);

		ResponseEntity<List<SessaoDTO>> response = controller.findByFilme(1);

		assertEquals(1, response.getBody().size());
		assertEquals(1, response.getBody().get(0).getFilmeId());
	}

	@Test
	void findBySala_DeveRetornarListaFiltrada() {
		List<SessaoDTO> lista = Arrays.asList(sessaoDTO);
		when(service.findBySala(1)).thenReturn(lista);

		ResponseEntity<List<SessaoDTO>> response = controller.findBySala(1);

		assertEquals(1, response.getBody().size());
		assertEquals(1, response.getBody().get(0).getSalaId());
	}

	@Test
	void findByData_DeveRetornarListaFiltrada() {
		// Atenção: O tipo da data no endpoint pode ser LocalDate ou LocalDateTime, dependendo da assinatura do método.
		// Se o endpoint espera LocalDate, ajuste para buscar apenas a parte da data.
		// Se o endpoint espera LocalDateTime, use dataHoraSessao.
		// Este teste assume que o endpoint aceita LocalDate ou LocalDateTime conforme sua implementação.

		// Exemplo para endpoint com LocalDate:
		// LocalDate data = dataHoraSessao.toLocalDate();
		// List<SessaoDTO> lista = Arrays.asList(sessaoDTO);
		// when(service.findByData(data)).thenReturn(lista);
		// ResponseEntity<List<SessaoDTO>> response = controller.findByData(data);

		// Como sua SessaoDTO usa LocalDateTime, mas o endpoint pode usar LocalDate, veja o comentário acima.
		// Para manter o teste funcional, vou fazer como se o endpoint aceitasse LocalDate (mais comum para busca por data):
		LocalDate data = dataHoraSessao.toLocalDate();
		List<SessaoDTO> lista = Arrays.asList(sessaoDTO);
		when(service.findByData(data)).thenReturn(lista);

		ResponseEntity<List<SessaoDTO>> response = controller.findByData(data);

		assertEquals(1, response.getBody().size());
		assertEquals(data, response.getBody().get(0).getDataHoraSessao().toLocalDate());
	}
}
