package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.AlimentoDTO;
import com.cleber.cinema.enums.TipoAlimento;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.repositories.AlimentoRepository;
import com.cleber.cinema.services.impl.AlimentoServiceImpl;
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
public class AlimentoServiceImplTest {

	@Mock
	private AlimentoRepository repository;

	@InjectMocks
	private AlimentoServiceImpl service;

	private Alimento alimento;
	private AlimentoDTO dto;

	@BeforeEach
	void setUp() {
		alimento = new Alimento();
		alimento.setId(1);
		alimento.setNome("Pipoca");
		alimento.setTipo(TipoAlimento.SALGADO);
		alimento.setPreco(15.90);
		alimento.setDescricao("Pipoca grande");

		dto = new AlimentoDTO();
		dto.setId(1);
		dto.setNome("Pipoca");
		dto.setTipo(TipoAlimento.SALGADO);
		dto.setPreco(15.90);
		dto.setDescricao("Pipoca grande");
	}

	@Test
	void create_DeveRetornarAlimentoDTO() {
		when(repository.save(any())).thenReturn(alimento);
		AlimentoDTO result = service.create(dto);
		assertEquals("Pipoca", result.getNome());
		verify(repository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDeAlimentos() {
		when(repository.findAll()).thenReturn(List.of(alimento));
		List<AlimentoDTO> result = service.findAll();
		assertEquals(1, result.size());
		assertEquals("Pipoca", result.getFirst().getNome());
	}

	@Test
	void findById_DeveRetornarAlimentoDTO() {
		when(repository.findById(1)).thenReturn(Optional.of(alimento));
		AlimentoDTO result = service.findById(1);
		assertEquals("Pipoca", result.getNome());
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	void update_DeveAtualizarCamposCorretamente() {
		when(repository.findById(1)).thenReturn(Optional.of(alimento));
		when(repository.save(any())).thenReturn(alimento);
		AlimentoDTO result = service.update(1, dto);
		assertEquals("Pipoca", result.getNome());
		verify(repository).save(alimento);
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(repository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(repository).deleteById(1);
	}

	@Test
	void findByTipo_DeveRetornarListaFiltrada() {
		when(repository.findByTipo(TipoAlimento.SALGADO)).thenReturn(List.of(alimento));
		List<AlimentoDTO> result = service.findByTipo(TipoAlimento.SALGADO);
		assertEquals(1, result.size());
		assertEquals(TipoAlimento.SALGADO, result.getFirst().getTipo());
	}

	@Test
	void findByNome_DeveRetornarListaFiltrada() {
		when(repository.findByNomeContainingIgnoreCase("Pipoca")).thenReturn(List.of(alimento));
		List<AlimentoDTO> result = service.findByNome("Pipoca");
		assertEquals(1, result.size());
		assertEquals("Pipoca", result.getFirst().getNome());
	}

	@Test
	void findByPrecoMaximo_DeveRetornarListaFiltrada() {
		when(repository.findByPrecoLessThanEqual(20.0)).thenReturn(List.of(alimento));
		List<AlimentoDTO> result = service.findByPrecoMaximo(20.0);
		assertEquals(1, result.size());
		assertTrue(result.getFirst().getPreco() <= 20.0);
	}
}
