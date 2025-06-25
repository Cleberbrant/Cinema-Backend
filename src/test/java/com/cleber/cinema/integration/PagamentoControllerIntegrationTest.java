package com.cleber.cinema.integration;

import com.cleber.cinema.dto.PagamentoCreateDTO;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.model.Pagamento;
import com.cleber.cinema.repositories.AlimentoRepository;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.repositories.PagamentoRepository;
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
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@Testcontainers
@Transactional
@DisplayName("Pagamento Controller - Testes de Integração")
class PagamentoControllerIntegrationTest {

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
		// Use a variável de ambiente do Spring Boot para a porta do WireMock
		registry.add("auth.service.url", () -> "http://localhost:${wiremock.server.port}");
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private FilmeRepository filmeRepository;

	@Autowired
	private AlimentoRepository alimentoRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private PagamentoCreateDTO pagamentoCreateDTO;
	private Filme filme;
	private Alimento alimento;
	private String usuarioId = UUID.randomUUID().toString();

	@BeforeEach
	void setUp() {
		pagamentoRepository.deleteAll();
		alimentoRepository.deleteAll();
		filmeRepository.deleteAll();

		filme = new Filme();
		filme.setTitulo("Vingadores: Ultimato");
		filme.setDuracao(java.time.LocalTime.of(3, 1));
		filme.setSinopse("Os heróis restantes se unem para desfazer as ações de Thanos.");
		filme.setValorIngresso(new java.math.BigDecimal("25.50"));
		filme.setDiretor("Anthony Russo");
		filme.setGenero(com.cleber.cinema.enums.GeneroFilme.ACAO);
		filme.setEmCartaz(true);
		filme = filmeRepository.save(filme);

		alimento = new Alimento();
		alimento.setNome("Combo Pipoca Grande");
		alimento.setPreco(20.0);
		alimento.setTipo(com.cleber.cinema.enums.TipoAlimento.COMBO);
		alimento = alimentoRepository.save(alimento);

		pagamentoCreateDTO = new PagamentoCreateDTO();
		pagamentoCreateDTO.setNumeroDoCartao("1234567890123456");
		pagamentoCreateDTO.setNomeImpresso("João Silva");
		pagamentoCreateDTO.setDataDeValidade("12/25");
		pagamentoCreateDTO.setCodigoDeSeguranca("123");
		pagamentoCreateDTO.setValorTotal(50.0);
		pagamentoCreateDTO.setFilmeId(filme.getId());
		pagamentoCreateDTO.setAlimentosIds(List.of(alimento.getId()));

		stubFor(get(urlEqualTo("/api/usuarios/" + usuarioId))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", "application/json")
						.withBody("\"João Silva\"")));
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
	@DisplayName("Deve criar pagamento com sucesso quando usuário autenticado")
	void deveCriarPagamentoComSucesso() throws Exception {
		String token = createJwtToken(usuarioId, "ROLE_USER");

		mockMvc.perform(post("/api/pagamentos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(pagamentoCreateDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.numeroDoCartao", is(pagamentoCreateDTO.getNumeroDoCartao())))
				.andExpect(jsonPath("$.valorTotal", is(pagamentoCreateDTO.getValorTotal())))
				.andExpect(jsonPath("$.usuarioId", is(usuarioId)))
				.andExpect(jsonPath("$.usuarioNome", is("João Silva")))
				.andExpect(jsonPath("$.filmeTitulo", is(filme.getTitulo())))
				.andExpect(jsonPath("$.alimentosNomes[0]", is(alimento.getNome())));
	}

	// Exemplo de mais um teste
	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar pagamento")
	void deveValidarCamposObrigatorios() throws Exception {
		String token = createJwtToken(usuarioId, "ROLE_USER");
		PagamentoCreateDTO invalidDTO = new PagamentoCreateDTO(); // Campos ausentes

		mockMvc.perform(post("/api/pagamentos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}

	private Pagamento criarPagamento(String usuarioId, Filme filme, List<Alimento> alimentos) {
		Pagamento pagamento = new Pagamento();
		pagamento.setNumeroDoCartao("1234567890123456");
		pagamento.setNomeImpresso("João Silva");
		pagamento.setDataDeValidade("12/25");
		pagamento.setCodigoDeSeguranca("123");
		pagamento.setValorTotal(50.0);
		pagamento.setDataPagamento(LocalDateTime.now());
		pagamento.setUsuarioId(usuarioId);
		pagamento.setFilme(filme);
		pagamento.setAlimentos(alimentos);
		return pagamentoRepository.save(pagamento);
	}
}
