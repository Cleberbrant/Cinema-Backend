package com.cleber.cinema.integration;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.model.Localidade;
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
@DisplayName("Localidade Controller - Testes de Integração")
class LocalidadeControllerIntegrationTest {

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
	private LocalidadeRepository localidadeRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private LocalidadeCreateDTO localidadeCreateDTO;

	@BeforeEach
	void setUp() {
		localidadeRepository.deleteAll();

		localidadeCreateDTO = new LocalidadeCreateDTO();
		localidadeCreateDTO.setEndereco("Rua das Flores, 123");
		localidadeCreateDTO.setCep("12345-678");
		localidadeCreateDTO.setReferencia("Próximo ao parque");
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
	@DisplayName("Deve criar localidade com sucesso quando usuário é ADMIN")
	void deveCriarLocalidadeComSucesso() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(post("/api/localidades")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(localidadeCreateDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.endereco", is(localidadeCreateDTO.getEndereco())))
				.andExpect(jsonPath("$.cep", is(localidadeCreateDTO.getCep())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@DisplayName("Deve retornar 403 ao criar localidade sem permissão ADMIN")
	void deveRetornar403AoCriarLocalidadeSemPermissao() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(post("/api/localidades")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(localidadeCreateDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar todas as localidades")
	void deveListarTodasAsLocalidades() throws Exception {
		Localidade localidade1 = criarLocalidade("Rua A", "11111-111", "Referência A");
		Localidade localidade2 = criarLocalidade("Rua B", "22222-222", "Referência B");

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/localidades")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].endereco", is(localidade1.getEndereco())))
				.andExpect(jsonPath("$[1].endereco", is(localidade2.getEndereco())));
	}

	@Test
	@DisplayName("Deve buscar localidade por ID")
	void deveBuscarLocalidadePorId() throws Exception {
		Localidade localidade = criarLocalidade("Rua Principal", "12345-678", "Próximo ao shopping");
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/localidades/{id}", localidade.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(localidade.getId())))
				.andExpect(jsonPath("$.endereco", is(localidade.getEndereco())));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar localidade inexistente")
	void deveRetornar404AoBuscarLocalidadeInexistente() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/localidades/{id}", 999)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar localidade com sucesso quando usuário é ADMIN")
	void deveAtualizarLocalidadeComSucesso() throws Exception {
		Localidade localidade = criarLocalidade("Rua Antiga", "00000-000", "Referência antiga");
		String token = createJwtToken("admin", "ROLE_ADMIN");

		LocalidadeCreateDTO updateDTO = new LocalidadeCreateDTO();
		updateDTO.setEndereco("Rua Nova");
		updateDTO.setCep("12345-678");
		updateDTO.setReferencia("Nova referência");

		mockMvc.perform(put("/api/localidades/{id}", localidade.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.endereco", is(updateDTO.getEndereco())))
				.andExpect(jsonPath("$.cep", is(updateDTO.getCep())));
	}

	@Test
	@DisplayName("Deve deletar localidade com sucesso quando usuário é ADMIN")
	void deveDeletarLocalidadeComSucesso() throws Exception {
		Localidade localidade = criarLocalidade("Rua X", "11111-111", "Ref X");
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(delete("/api/localidades/{id}", localidade.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve buscar localidades por CEP")
	void deveBuscarLocalidadesPorCep() throws Exception {
		criarLocalidade("Rua A", "12345-678", "Ref A");
		criarLocalidade("Rua B", "12345-000", "Ref B");
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/localidades/cep/{cep}", "12345")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].cep", containsString("12345")))
				.andExpect(jsonPath("$[1].cep", containsString("12345")));
	}

	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar localidade")
	void deveValidarCamposObrigatoriosAoCriarLocalidade() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		LocalidadeCreateDTO invalidDTO = new LocalidadeCreateDTO(); // Campos obrigatórios ausentes

		mockMvc.perform(post("/api/localidades")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}

	private Localidade criarLocalidade(String endereco, String cep, String referencia) {
		Localidade localidade = new Localidade();
		localidade.setEndereco(endereco);
		localidade.setCep(cep);
		localidade.setReferencia(referencia);
		return localidadeRepository.save(localidade);
	}
}
