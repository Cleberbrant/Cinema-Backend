package com.cleber.cinema.integration;

import com.cleber.cinema.dto.AlimentoDTO;
import com.cleber.cinema.enums.TipoAlimento;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.repositories.AlimentoRepository;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DisplayName("Alimento Controller - Testes de Integração")
public class AlimentoControllerIntegrationTest {

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
	private AlimentoRepository alimentoRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private AlimentoDTO alimentoDTO;

	@BeforeEach
	void setUp() {
		alimentoRepository.deleteAll();

		alimentoDTO = new AlimentoDTO();
		alimentoDTO.setNome("Combo Pipoca Grande");
		alimentoDTO.setTipo(TipoAlimento.COMBO);
		alimentoDTO.setPreco(25.0);
		alimentoDTO.setDescricao("Combo com pipoca grande e refrigerante");
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
	@DisplayName("Deve criar alimento com sucesso quando usuário é ADMIN")
	void deveCriarAlimentoComSucesso() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(post("/api/alimentos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(alimentoDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome", is(alimentoDTO.getNome())))
				.andExpect(jsonPath("$.tipo", is(alimentoDTO.getTipo().toString())))
				.andExpect(jsonPath("$.preco", is(alimentoDTO.getPreco())))
				.andExpect(jsonPath("$.descricao", is(alimentoDTO.getDescricao())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@DisplayName("Deve retornar 403 ao criar alimento sem permissão ADMIN")
	void deveRetornar403AoCriarAlimentoSemPermissao() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(post("/api/alimentos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(alimentoDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar todos os alimentos")
	void deveListarTodosOsAlimentos() throws Exception {
		Alimento alimento1 = criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		Alimento alimento2 = criarAlimento("Pipoca Média", TipoAlimento.PIPOCA, 15.0, "Pipoca média salgada");

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/alimentos")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].nome", is(alimento1.getNome())))
				.andExpect(jsonPath("$[1].nome", is(alimento2.getNome())));
	}

	@Test
	@DisplayName("Deve buscar alimento por ID")
	void deveBuscarAlimentoPorId() throws Exception {
		Alimento alimento = criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/alimentos/{id}", alimento.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(alimento.getId())))
				.andExpect(jsonPath("$.nome", is(alimento.getNome())));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar alimento inexistente")
	void deveRetornar404AoBuscarAlimentoInexistente() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/alimentos/{id}", 999)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar alimento com sucesso quando usuário é ADMIN")
	void deveAtualizarAlimentoComSucesso() throws Exception {
		Alimento alimento = criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		String token = createJwtToken("admin", "ROLE_ADMIN");

		AlimentoDTO updateDTO = new AlimentoDTO();
		updateDTO.setNome("Combo Pipoca Grande Atualizado");
		updateDTO.setTipo(TipoAlimento.COMBO);
		updateDTO.setPreco(30.0);
		updateDTO.setDescricao("Descrição atualizada");

		mockMvc.perform(put("/api/alimentos/{id}", alimento.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome", is(updateDTO.getNome())))
				.andExpect(jsonPath("$.preco", is(updateDTO.getPreco())));
	}

	@Test
	@DisplayName("Deve deletar alimento com sucesso quando usuário é ADMIN")
	void deveDeletarAlimentoComSucesso() throws Exception {
		Alimento alimento = criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(delete("/api/alimentos/{id}", alimento.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve buscar alimentos por tipo")
	void deveBuscarAlimentosPorTipo() throws Exception {
		criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		criarAlimento("Pipoca Média", TipoAlimento.PIPOCA, 15.0, "Pipoca média salgada");
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/alimentos/tipo/{tipo}", TipoAlimento.COMBO)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].tipo", is(TipoAlimento.COMBO.toString())));
	}

	@Test
	@DisplayName("Deve buscar alimentos por nome")
	void deveBuscarAlimentosPorNome() throws Exception {
		criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		criarAlimento("Pipoca Média", TipoAlimento.PIPOCA, 15.0, "Pipoca média salgada");
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/alimentos/nome/{nome}", "Pipoca")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].nome", containsString("Pipoca")))
				.andExpect(jsonPath("$[1].nome", containsString("Pipoca")));
	}

	@Test
	@DisplayName("Deve buscar alimentos por preço máximo")
	void deveBuscarAlimentosPorPrecoMaximo() throws Exception {
		criarAlimento("Combo Pipoca Grande", TipoAlimento.COMBO, 25.0, "Combo com pipoca grande e refrigerante");
		criarAlimento("Pipoca Média", TipoAlimento.PIPOCA, 15.0, "Pipoca média salgada");
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/alimentos/preco/{preco}", 20.0)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].preco", lessThanOrEqualTo(20.0)));
	}

	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar alimento")
	void deveValidarCamposObrigatoriosAoCriarAlimento() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");
		AlimentoDTO invalidDTO = new AlimentoDTO(); // Campos obrigatórios ausentes

		mockMvc.perform(post("/api/alimentos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}

	private Alimento criarAlimento(String nome, TipoAlimento tipo, Double preco, String descricao) {
		Alimento alimento = new Alimento();
		alimento.setNome(nome);
		alimento.setTipo(tipo);
		alimento.setPreco(preco);
		alimento.setDescricao(descricao);
		return alimentoRepository.save(alimento);
	}
}
