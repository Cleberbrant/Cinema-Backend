package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.services.impl.FilmeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmeServiceImplTest {

	@Mock
	private FilmeRepository repository;

	@InjectMocks
	private FilmeServiceImpl service;

	private Filme filme;
	private FilmeDTO dto;

	@BeforeEach
	void setUp() {
		filme = new Filme();
		filme.setId(1);
		filme.setTitulo("Interestelar");
		filme.setDuracao(LocalTime.of(2, 49)); // 2h49m
		filme.setSinopse("Ficção científica");
		filme.setGenero(GeneroFilme.FICCAO_CIENTIFICA);
		filme.setDiretor("Nolan");
		filme.setValorIngresso(new BigDecimal("45.90"));
		filme.setEmCartaz(true);

		dto = new FilmeDTO();
		dto.setId(1);
		dto.setTitulo("Interestelar");
		dto.setDuracao(LocalTime.of(2, 49));
		dto.setSinopse("Ficção científica");
		dto.setGenero(GeneroFilme.FICCAO_CIENTIFICA);
		dto.setDiretor("Nolan");
		dto.setValorIngresso(new BigDecimal("45.90"));
		dto.setEmCartaz(true);
	}

	@Test
	void create_DeveRetornarFilmeDTO() {
		when(repository.save(any())).thenReturn(filme);
		FilmeDTO result = service.create(dto);
		assertEquals("Interestelar", result.getTitulo());
		assertEquals(LocalTime.of(2, 49), result.getDuracao());
		assertEquals(new BigDecimal("45.90"), result.getValorIngresso());
		verify(repository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDeFilmes() {
		when(repository.findAll()).thenReturn(List.of(filme));
		List<FilmeDTO> result = service.findAll();
		assertEquals(1, result.size());
		assertEquals("Interestelar", result.getFirst().getTitulo());
	}

	@Test
	void findById_DeveRetornarFilmeDTO() {
		when(repository.findById(1)).thenReturn(Optional.of(filme));
		FilmeDTO result = service.findById(1);
		assertEquals("Interestelar", result.getTitulo());
		assertEquals(LocalTime.of(2, 49), result.getDuracao());
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	void update_DeveAtualizarCamposCorretamente() {
		when(repository.findById(1)).thenReturn(Optional.of(filme));
		when(repository.save(any())).thenReturn(filme);
		FilmeDTO result = service.update(1, dto);
		assertEquals("Interestelar", result.getTitulo());
		verify(repository).save(filme);
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(repository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(repository).deleteById(1);
	}

	@Test
	void findByEmCartaz_DeveRetornarListaFiltrada() {
		when(repository.findByEmCartaz(true)).thenReturn(List.of(filme));
		List<FilmeDTO> result = service.findByEmCartaz(true);
		assertEquals(1, result.size());
		assertTrue(result.getFirst().isEmCartaz());
	}

	@Test
	void findByGenero_DeveRetornarListaFiltrada() {
		when(repository.findByGenero(GeneroFilme.FICCAO_CIENTIFICA)).thenReturn(List.of(filme));
		List<FilmeDTO> result = service.findByGenero(GeneroFilme.FICCAO_CIENTIFICA);
		assertEquals(1, result.size());
		assertEquals(GeneroFilme.FICCAO_CIENTIFICA, result.getFirst().getGenero());
	}

	@Test
	void findByTitulo_DeveRetornarListaFiltrada() {
		when(repository.findByTituloContainingIgnoreCase("Interestelar")).thenReturn(List.of(filme));
		List<FilmeDTO> result = service.findByTitulo("Interestelar");
		assertEquals(1, result.size());
		assertEquals("Interestelar", result.getFirst().getTitulo());
	}
}
