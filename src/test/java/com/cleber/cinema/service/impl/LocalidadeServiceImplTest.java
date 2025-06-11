package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.repositories.LocalidadeRepository;
import com.cleber.cinema.services.impl.LocalidadeServiceImpl;
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
public class LocalidadeServiceImplTest {

	@Mock
	private LocalidadeRepository repository;

	@InjectMocks
	private LocalidadeServiceImpl service;

	private Localidade localidade;
	private LocalidadeCreateDTO createDTO;

	@BeforeEach
	void setUp() {
		localidade = new Localidade();
		localidade.setId(1);
		localidade.setEndereco("Rua Teste");
		localidade.setCep("12345-678");
		localidade.setReferencia("Próximo ao shopping");

		createDTO = new LocalidadeCreateDTO();
		createDTO.setEndereco("Rua Teste");
		createDTO.setCep("12345-678");
		createDTO.setReferencia("Próximo ao shopping");
	}

	@Test
	void create_DeveRetornarLocalidadeDTO() {
		when(repository.save(any())).thenReturn(localidade);
		LocalidadeDTO result = service.create(createDTO);
		assertEquals("Rua Teste", result.getEndereco());
		verify(repository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDeLocalidades() {
		when(repository.findAll()).thenReturn(List.of(localidade));
		List<LocalidadeDTO> result = service.findAll();
		assertEquals(1, result.size());
		assertEquals("Rua Teste", result.getFirst().getEndereco());
	}

	@Test
	void findById_DeveRetornarLocalidadeDTO() {
		when(repository.findById(1)).thenReturn(Optional.of(localidade));
		LocalidadeDTO result = service.findById(1);
		assertEquals("Rua Teste", result.getEndereco());
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	void update_DeveAtualizarCamposCorretamente() {
		when(repository.findById(1)).thenReturn(Optional.of(localidade));
		when(repository.save(any())).thenReturn(localidade);
		LocalidadeDTO result = service.update(1, createDTO);
		assertEquals("Rua Teste", result.getEndereco());
		verify(repository).save(localidade);
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(repository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(repository).deleteById(1);
	}

	@Test
	void findByCep_DeveRetornarListaFiltrada() {
		when(repository.findByCepStartingWith("12345")).thenReturn(List.of(localidade));
		List<LocalidadeDTO> result = service.findByCep("12345");
		assertEquals(1, result.size());
		assertEquals("12345-678", result.getFirst().getCep());
	}
}
