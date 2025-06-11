package com.cleber.cinema.controller;

import com.cleber.cinema.dto.AlimentoDTO;
import com.cleber.cinema.enums.TipoAlimento;
import com.cleber.cinema.services.AlimentoService;
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
class AlimentoControllerTest {

	@Mock
	private AlimentoService service;

	@InjectMocks
	private AlimentoController controller;

	private AlimentoDTO alimentoDTO;

	@BeforeEach
	void setUp() {
		alimentoDTO = new AlimentoDTO();
		alimentoDTO.setNome("Pipoca");
		alimentoDTO.setPreco(15.90);
		alimentoDTO.setTipo(TipoAlimento.SALGADO);
	}

	@Test
	void create_DeveRetornarStatusCreated() {
		when(service.create(any(AlimentoDTO.class))).thenReturn(alimentoDTO);

		ResponseEntity<AlimentoDTO> response = controller.create(alimentoDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Pipoca", response.getBody().getNome());
		verify(service, times(1)).create(alimentoDTO);
	}

	@Test
	void findAll_DeveRetornarListaCompleta() {
		List<AlimentoDTO> lista = Arrays.asList(alimentoDTO);
		when(service.findAll()).thenReturn(lista);

		ResponseEntity<List<AlimentoDTO>> response = controller.findAll();

		assertEquals(1, response.getBody().size());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void findById_DeveRetornarAlimentoExistente() {
		when(service.findById(1)).thenReturn(alimentoDTO);

		ResponseEntity<AlimentoDTO> response = controller.findById(1);

		assertEquals("Pipoca", response.getBody().getNome());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void update_DeveRetornarAlimentoAtualizado() {
		when(service.update(eq(1), any(AlimentoDTO.class))).thenReturn(alimentoDTO);

		ResponseEntity<AlimentoDTO> response = controller.update(1, alimentoDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(service, times(1)).update(1, alimentoDTO);
	}

	@Test
	void delete_DeveRetornarNoContent() {
		doNothing().when(service).delete(1);

		ResponseEntity<Void> response = controller.delete(1);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(service, times(1)).delete(1);
	}

	@Test
	void findByTipo_DeveRetornarListaFiltrada() {
		List<AlimentoDTO> lista = Arrays.asList(alimentoDTO);
		when(service.findByTipo(TipoAlimento.SALGADO)).thenReturn(lista);

		ResponseEntity<List<AlimentoDTO>> response = controller.findByTipo(TipoAlimento.SALGADO);

		assertEquals(1, response.getBody().size());
		assertEquals(TipoAlimento.SALGADO, response.getBody().get(0).getTipo());
	}

	@Test
	void findByNome_DeveRetornarListaCorreta() {
		List<AlimentoDTO> lista = Arrays.asList(alimentoDTO);
		when(service.findByNome("Pipoca")).thenReturn(lista);

		ResponseEntity<List<AlimentoDTO>> response = controller.findByNome("Pipoca");

		assertEquals(1, response.getBody().size());
		assertEquals("Pipoca", response.getBody().get(0).getNome());
	}

	@Test
	void findByPrecoMaximo_DeveFiltrarCorretamente() {
		List<AlimentoDTO> lista = Arrays.asList(alimentoDTO);
		when(service.findByPrecoMaximo(20.0)).thenReturn(lista);

		ResponseEntity<List<AlimentoDTO>> response = controller.findByPrecoMaximo(20.0);

		assertEquals(1, response.getBody().size());
		assertTrue(response.getBody().get(0).getPreco() <= 20.0);
	}
}
