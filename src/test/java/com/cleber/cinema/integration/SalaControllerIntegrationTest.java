package com.cleber.cinema.integration;

import com.cleber.cinema.dto.SalaCreateDTO;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.model.Sala;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.repositories.LocalidadeRepository;
import com.cleber.cinema.repositories.SalaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DisplayName("Sala Controller - Testes de Integração")
class SalaControllerIntegrationTest {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
			.withDatabaseName("cinema_test")
			.withUsername("test")
			.withPassword("test");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
		registry.add("jwt.secret", () -> "3nG8xZaQrY7cW2fA7qbq6zTyZ8bC3dA2wS7gN1mK9jF0hL4tUoP6iBvE3nG8xZaQ");
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SalaRepository salaRepository;

	@Autowired
	private CinemaRepository cinemaRepository;

	@Autowired
	private LocalidadeRepository localidadeRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private SalaCreateDTO salaCreateDTO;
	private Cinema cinema;

	@BeforeEach
	void setUp() {
		salaRepository.deleteAll();
		cinemaRepository.deleteAll();
		localidadeRepository.deleteAll();

		// Criar cinema com localidade para associação
		cinema = criarCinemaComLocalidade("Cinema Central");

		salaCreateDTO = new SalaCreateDTO();
		salaCreateDTO.setNumeroDaSala(1);
		salaCreateDTO.setCapacidade(100);
		salaCreateDTO.setMapaDaSala("Mapa A1");
		salaCreateDTO.setTecnologia("IMAX");
		salaCreateDTO.setCinemaId(cinema.getId());
	}

	private String createJwtToken(String userId, String role) {
		return Jwts.builder()
				.setSubject(userId)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();
	}

	private Cinema criarCinemaComLocalidade(String nome) {
		Localidade localidade = new Localidade();
		localidade.setEndereco("Rua Principal, 123");
		localidade.setCep("12345-678");
		localidade.setReferencia("Próximo ao shopping");
		localidade = localidadeRepository.save(localidade);

		Cinema cinema = new Cinema();
		cinema.setNome(nome);
		cinema.setLocalidade(localidade);
		return cinemaRepository.save(cinema);
	}

	private Sala criarSala(Integer numero, Integer capacidade, String tecnologia, Cinema cinema) {
		Sala sala = new Sala();
		sala.setNumeroDaSala(numero);
		sala.setCapacidade(capacidade);
		sala.setTecnologia(tecnologia);
		sala.setCinema(cinema);
		return salaRepository.save(sala);
	}

	@Test
	@DisplayName("Deve criar sala com sucesso quando usuário é ADMIN")
	void deveCriarSalaComSucesso() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(post("/api/salas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(salaCreateDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.numeroDaSala", is(salaCreateDTO.getNumeroDaSala())))
				.andExpect(jsonPath("$.tecnologia", is(salaCreateDTO.getTecnologia())))
				.andExpect(jsonPath("$.cinemaId", is(cinema.getId())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@DisplayName("Deve retornar 403 ao criar sala sem permissão ADMIN")
	void deveRetornar403AoCriarSalaSemPermissao() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(post("/api/salas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(salaCreateDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar todas as salas")
	void deveListarTodasAsSalas() throws Exception {
		Sala sala1 = criarSala(1, 100, "IMAX", cinema);
		Sala sala2 = criarSala(2, 80, "3D", cinema);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/salas")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].numeroDaSala", is(sala1.getNumeroDaSala())))
				.andExpect(jsonPath("$[1].numeroDaSala", is(sala2.getNumeroDaSala())));
	}

	@Test
	@DisplayName("Deve buscar sala por ID")
	void deveBuscarSalaPorId() throws Exception {
		Sala sala = criarSala(1, 100, "IMAX", cinema);
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/salas/{id}", sala.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(sala.getId())))
				.andExpect(jsonPath("$.numeroDaSala", is(sala.getNumeroDaSala())));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar sala inexistente")
	void deveRetornar404AoBuscarSalaInexistente() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/salas/{id}", 999)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar sala com sucesso quando usuário é ADMIN")
	void deveAtualizarSalaComSucesso() throws Exception {
		Sala sala = criarSala(1, 100, "IMAX", cinema);
		String token = createJwtToken("admin", "ROLE_ADMIN");

		SalaCreateDTO updateDTO = new SalaCreateDTO();
		updateDTO.setNumeroDaSala(2);
		updateDTO.setCapacidade(120);
		updateDTO.setMapaDaSala("Mapa B2");
		updateDTO.setTecnologia("4DX");
		updateDTO.setCinemaId(cinema.getId());

		mockMvc.perform(put("/api/salas/{id}", sala.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numeroDaSala", is(updateDTO.getNumeroDaSala())))
				.andExpect(jsonPath("$.tecnologia", is(updateDTO.getTecnologia())));
	}

	@Test
	@DisplayName("Deve deletar sala com sucesso quando usuário é ADMIN")
	void deveDeletarSalaComSucesso() throws Exception {
		Sala sala = criarSala(1, 100, "IMAX", cinema);
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(delete("/api/salas/{id}", sala.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve buscar salas por cinema")
	void deveBuscarSalasPorCinema() throws Exception {
		Cinema outroCinema = criarCinemaComLocalidade("Cinema Norte");

		criarSala(1, 100, "IMAX", cinema);
		criarSala(2, 80, "3D", cinema);
		criarSala(3, 120, "4DX", outroCinema); // Não deve ser retornado

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/salas/cinema/{cinemaId}", cinema.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].cinemaId", is(cinema.getId())))
				.andExpect(jsonPath("$[1].cinemaId", is(cinema.getId())));
	}

	@Test
	@DisplayName("Deve buscar salas por tecnologia")
	void deveBuscarSalasPorTecnologia() throws Exception {
		criarSala(1, 100, "IMAX", cinema);
		criarSala(2, 80, "3D", cinema);
		criarSala(3, 120, "IMAX", cinema);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/salas/tecnologia/{tecnologia}", "IMAX")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].tecnologia", is("IMAX")))
				.andExpect(jsonPath("$[1].tecnologia", is("IMAX")));
	}

	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar sala")
	void deveValidarCamposObrigatoriosAoCriarSala() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		SalaCreateDTO invalidDTO = new SalaCreateDTO(); // Campos obrigatórios ausentes

		mockMvc.perform(post("/api/salas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}
}
