package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.SessaoCreateDTO;
import com.cleber.cinema.dto.SessaoDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.model.Sala;
import com.cleber.cinema.model.Sessao;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.repositories.SalaRepository;
import com.cleber.cinema.repositories.SessaoRepository;
import com.cleber.cinema.services.impl.SessaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessaoServiceImplTest {

	@Mock
	private SessaoRepository sessaoRepository;

	@Mock
	private SalaRepository salaRepository;

	@Mock
	private FilmeRepository filmeRepository;

	@InjectMocks
	private SessaoServiceImpl service;

	private SessaoCreateDTO createDTO;
	private Sessao sessao; // Variável de instância para reutilização nos testes

	@BeforeEach
	void setUp() {
		// Configurar Cinema
		Cinema cinema = new Cinema();
		cinema.setId(1);

		// Configurar Sala
		Sala sala = new Sala();
		sala.setId(1);
		sala.setCinema(cinema);

		// Configurar Filme
		Filme filme = new Filme();
		filme.setId(1);

		// Configurar Sessão de teste
		sessao = new Sessao();
		sessao.setId(1);
		sessao.setDataHoraSessao(LocalDateTime.now());
		sessao.setSala(sala);
		sessao.setFilme(filme);

		// Configurar DTO
		createDTO = new SessaoCreateDTO();
		createDTO.setDataHoraSessao(LocalDateTime.now());
		createDTO.setSalaId(1);
		createDTO.setFilmeId(1);
	}

	@Test
	void create_DeveRetornarSessaoDTO() {
		when(salaRepository.findById(1)).thenReturn(Optional.of(sessao.getSala()));
		when(filmeRepository.findById(1)).thenReturn(Optional.of(sessao.getFilme()));
		when(sessaoRepository.save(any())).thenReturn(sessao);

		SessaoDTO result = service.create(createDTO);

		assertNotNull(result);
		assertEquals(1, result.getSalaId());
		assertEquals(1, result.getFilmeId());
		verify(sessaoRepository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDeSessoes() {
		when(sessaoRepository.findAll()).thenReturn(List.of(sessao));
		List<SessaoDTO> result = service.findAll();

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getSalaId());
		assertEquals(1, result.get(0).getFilmeId());
	}

	@Test
	void findById_DeveRetornarSessaoDTO() {
		when(sessaoRepository.findById(1)).thenReturn(Optional.of(sessao));
		SessaoDTO result = service.findById(1);

		assertNotNull(result);
		assertEquals(1, result.getSalaId());
		assertEquals(1, result.getFilmeId());
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(sessaoRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	void update_DeveAtualizarCamposCorretamente() {
		when(sessaoRepository.findById(1)).thenReturn(Optional.of(sessao));
		when(salaRepository.findById(1)).thenReturn(Optional.of(sessao.getSala()));
		when(filmeRepository.findById(1)).thenReturn(Optional.of(sessao.getFilme()));
		when(sessaoRepository.save(any())).thenReturn(sessao);

		SessaoDTO result = service.update(1, createDTO);

		assertNotNull(result);
		assertEquals(1, result.getSalaId());
		verify(sessaoRepository).save(any());
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(sessaoRepository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(sessaoRepository).deleteById(1);
	}

	@Test
	void findByFilme_DeveRetornarListaFiltrada() {
		when(sessaoRepository.findByFilmeId(1)).thenReturn(List.of(sessao));
		List<SessaoDTO> result = service.findByFilme(1);

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getFilmeId());
	}

	@Test
	void findBySala_DeveRetornarListaFiltrada() {
		when(sessaoRepository.findBySalaId(1)).thenReturn(List.of(sessao));
		List<SessaoDTO> result = service.findBySala(1);

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getSalaId());
	}

	@Test
	void findByData_DeveRetornarListaFiltrada() {
		LocalDate data = LocalDate.of(2025, 6, 11);
		LocalDateTime start = data.atStartOfDay();
		LocalDateTime end = data.atTime(LocalTime.MAX);

		when(sessaoRepository.findByDataHoraSessaoBetween(start, end)).thenReturn(List.of(sessao));
		List<SessaoDTO> result = service.findByData(data);

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getId());
	}
}
