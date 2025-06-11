package com.cleber.cinema.service.impl;

import com.cleber.cinema.dto.PagamentoCreateDTO;
import com.cleber.cinema.dto.PagamentoDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.model.Pagamento;
import com.cleber.cinema.repositories.AlimentoRepository;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.repositories.PagamentoRepository;
import com.cleber.cinema.services.impl.PagamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceImplTest {

	@Mock
	private PagamentoRepository pagamentoRepository;

	@Mock
	private FilmeRepository filmeRepository;

	@Mock
	private AlimentoRepository alimentoRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private PagamentoServiceImpl service;

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
		createDTO.setAlimentosIds(List.of(1, 2));
	}

	@Test
	@WithMockUser(username = "user123")
	void create_DeveRetornarPagamentoDTO() {
		when(filmeRepository.findById(1)).thenReturn(Optional.of(new Filme()));
		when(alimentoRepository.findAllById(List.of(1, 2))).thenReturn(List.of(new Alimento(), new Alimento()));

		when(pagamentoRepository.save(any())).thenReturn(new Pagamento());

		PagamentoDTO result = service.create(createDTO);

		assertNotNull(result);
		verify(pagamentoRepository).save(any());
	}

	@Test
	void findAll_DeveRetornarListaDePagamentos() {
		when(pagamentoRepository.findAll()).thenReturn(List.of(new Pagamento()));
		List<PagamentoDTO> result = service.findAll();
		assertEquals(1, result.size());
	}

	@Test
	void findById_DeveRetornarPagamentoDTO() {
		when(pagamentoRepository.findById(1)).thenReturn(Optional.of(new Pagamento()));
		PagamentoDTO result = service.findById(1);
		assertNotNull(result);
	}

	@Test
	void findById_DeveLancarExcecaoQuandoNaoEncontrado() {
		when(pagamentoRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
	}

	@Test
	@WithMockUser(username = "user123")
	void update_DeveAtualizarCamposCorretamente() {
		when(pagamentoRepository.findById(1)).thenReturn(Optional.of(new Pagamento()));
		when(filmeRepository.findById(1)).thenReturn(Optional.of(new Filme()));
		when(alimentoRepository.findAllById(List.of(1, 2))).thenReturn(List.of(new Alimento(), new Alimento()));
		when(pagamentoRepository.save(any())).thenReturn(new Pagamento());

		PagamentoDTO result = service.update(1, createDTO);

		assertNotNull(result);
		verify(pagamentoRepository).save(any());
	}

	@Test
	void delete_DeveChamarDeleteNoRepository() {
		when(pagamentoRepository.existsById(1)).thenReturn(true);
		service.delete(1);
		verify(pagamentoRepository).deleteById(1);
	}

	@Test
	void findByFilme_DeveRetornarListaFiltrada() {
		when(pagamentoRepository.findByFilmeId(1)).thenReturn(List.of(new Pagamento()));
		List<PagamentoDTO> result = service.findByFilme(1);
		assertEquals(1, result.size());
	}
}
