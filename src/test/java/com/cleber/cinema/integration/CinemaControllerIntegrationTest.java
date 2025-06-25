package com.cleber.cinema.integration;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.model.Cinema;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.repositories.CinemaRepository;
import com.cleber.cinema.repositories.LocalidadeRepository;
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
@DisplayName("Cinema Controller - Testes de Integração")
class CinemaControllerIntegrationTest {

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
	private CinemaRepository cinemaRepository;

	@Autowired
	private LocalidadeRepository localidadeRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private CinemaCreateDTO cinemaCreateDTO;
	private LocalidadeDTO localidadeDTO;

	@BeforeEach
	void setUp() {
		cinemaRepository.deleteAll();
		if (localidadeRepository != null) localidadeRepository.deleteAll();

		localidadeDTO = new LocalidadeDTO();
		localidadeDTO.setEndereco("Rua Principal, 123");
		localidadeDTO.setCep("12345-678");
		localidadeDTO.setReferencia("Próximo ao shopping");

		cinemaCreateDTO = new CinemaCreateDTO();
		cinemaCreateDTO.setNome("CineMax");
		cinemaCreateDTO.setLocalidade(localidadeDTO);
	}

	private Cinema criarCinema(String nome, LocalidadeDTO localidadeDTO) {
		Localidade localidade = new Localidade();
		localidade.setEndereco(localidadeDTO.getEndereco());
		localidade.setCep(localidadeDTO.getCep());
		localidade.setReferencia(localidadeDTO.getReferencia());
		localidade = localidadeRepository.save(localidade);

		Cinema cinema = Cinema.builder()
				.nome(nome)
				.localidade(localidade)
				.build();

		return cinemaRepository.save(cinema);
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

	@Test
	@DisplayName("Deve criar cinema com sucesso quando usuário é ADMIN")
	void deveCriarCinemaComSucesso() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(post("/api/cinemas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cinemaCreateDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome", is(cinemaCreateDTO.getNome())))
				.andExpect(jsonPath("$.localidade.endereco", is(localidadeDTO.getEndereco())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@DisplayName("Deve retornar 403 ao criar cinema sem permissão ADMIN")
	void deveRetornar403AoCriarCinemaSemPermissao() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(post("/api/cinemas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cinemaCreateDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar todos os cinemas")
	void deveListarTodosOsCinemas() throws Exception {
		Cinema cinema1 = criarCinema("CineMax", localidadeDTO);
		Cinema cinema2 = criarCinema("CineArt", localidadeDTO);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/cinemas")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].nome", is(cinema1.getNome())))
				.andExpect(jsonPath("$[1].nome", is(cinema2.getNome())));
	}

	@Test
	@DisplayName("Deve buscar cinema por ID")
	void deveBuscarCinemaPorId() throws Exception {
		Cinema cinema = criarCinema("CineMax", localidadeDTO);
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/cinemas/{id}", cinema.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(cinema.getId())))
				.andExpect(jsonPath("$.nome", is(cinema.getNome())));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar cinema inexistente")
	void deveRetornar404AoBuscarCinemaInexistente() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/cinemas/{id}", 999)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar cinema com sucesso quando usuário é ADMIN")
	void deveAtualizarCinemaComSucesso() throws Exception {
		Cinema cinema = criarCinema("CineMax", localidadeDTO);
		String token = createJwtToken("admin", "ROLE_ADMIN");

		CinemaCreateDTO updateDTO = new CinemaCreateDTO();
		updateDTO.setNome("CineMax Premium");
		updateDTO.setLocalidade(localidadeDTO);

		mockMvc.perform(put("/api/cinemas/{id}", cinema.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome", is(updateDTO.getNome())));
	}

	@Test
	@DisplayName("Deve deletar cinema com sucesso quando usuário é ADMIN")
	void deveDeletarCinemaComSucesso() throws Exception {
		Cinema cinema = criarCinema("CineMax", localidadeDTO);
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(delete("/api/cinemas/{id}", cinema.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve buscar cinemas por nome")
	void deveBuscarCinemasPorNome() throws Exception {
		criarCinema("CineMax", localidadeDTO);
		criarCinema("CineArt", localidadeDTO);
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/cinemas/nome/{nome}", "Cine")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].nome", containsString("Cine")))
				.andExpect(jsonPath("$[1].nome", containsString("Cine")));
	}

	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar cinema")
	void deveValidarCamposObrigatoriosAoCriarCinema() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		CinemaCreateDTO invalidDTO = new CinemaCreateDTO(); // Sem nome e localidade

		mockMvc.perform(post("/api/cinemas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}
}
