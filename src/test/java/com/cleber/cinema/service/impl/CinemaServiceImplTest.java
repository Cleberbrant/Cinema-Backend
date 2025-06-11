package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.services.impl.CinemaServiceImpl;
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
public class CinemaServiceImplTest {

	@Mock
	private CinemaRepository repository;

	@InjectMocks
	private CinemaServiceImpl service;

	private Cinema cinema;
	private CinemaCreateDTO createDTO;

	@BeforeEach
	void setUp() {
		Localidade localidade = new Localidade();
		localidade.setId(1);
		localidade.setEndereco("Rua Teste");
		localidade.setCep("12345-678");
		localidade.setReferencia("Próximo ao shopping");

		cinema = new Cinema();
		cinema.setId(1);
		cinema.setNome("CineArt");
		cinema.setLocalidade(localidade);

		LocalidadeDTO localidadeDTO = new LocalidadeDTO();
		localidadeDTO.setId(1);
		localidadeDTO.setEndereco("Rua Teste");
		localidadeDTO.setCep("12345-678");
		localidadeDTO.setReferencia("Próximo ao shopping");

		createDTO = new CinemaCreateDTO();
		createDTO.setNome("CineArt");
		createDTO.setLocalidade(localidadeDTO);
	}

	@Test
	void create_DeveRetornarCinemaDTO() {
		when(repository.save(any())).thenReturn(cinema);
		CinemaDTO result = service.create(createDTO);
		assertEquals("CineArt", result.getNome());
		verify(repository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDeCinemas() {
		when(repository.findAll()).thenReturn(List.of(cinema));
		List<CinemaDTO> result = service.findAll();
		assertEquals(1, result.size());
		assertEquals("CineArt", result.getFirst().getNome());
	}

	@Test
	void findById_DeveRetornarCinemaDTO() {
		when(repository.findById(1)).thenReturn(Optional.of(cinema));
		CinemaDTO result = service.findById(1);
		assertEquals("CineArt", result.getNome());
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	void update_DeveAtualizarCamposCorretamente() {
		when(repository.findById(1)).thenReturn(Optional.of(cinema));
		when(repository.save(any())).thenReturn(cinema);
		CinemaDTO result = service.update(1, createDTO);
		assertEquals("CineArt", result.getNome());
		verify(repository).save(cinema);
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(repository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(repository).deleteById(1);
	}

	@Test
	void findByNome_DeveRetornarListaFiltrada() {
		when(repository.findByNomeContainingIgnoreCase("CineArt")).thenReturn(List.of(cinema));
		List<CinemaDTO> result = service.findByNome("CineArt");
		assertEquals(1, result.size());
		assertEquals("CineArt", result.getFirst().getNome());
	}
}
