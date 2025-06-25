package com.cleber.cinema.integration;

import com.cleber.cinema.dto.SessaoCreateDTO;
import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.model.*;
import com.cleber.cinema.repositories.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DisplayName("Sessão Controller - Testes de Integração")
class SessaoControllerIntegrationTest {

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
	private SessaoRepository sessaoRepository;

	@Autowired
	private SalaRepository salaRepository;

	@Autowired
	private FilmeRepository filmeRepository;

	@Autowired
	private CinemaRepository cinemaRepository;

	@Autowired
	private LocalidadeRepository localidadeRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private SessaoCreateDTO sessaoCreateDTO;
	private Sala sala;
	private Filme filme;

	@BeforeEach
	void setUp() {
		sessaoRepository.deleteAll();
		salaRepository.deleteAll();
		filmeRepository.deleteAll();
		cinemaRepository.deleteAll();
		localidadeRepository.deleteAll();

		// Criar localidade e cinema válidos
		Localidade localidade = new Localidade();
		localidade.setEndereco("Rua Principal, 123");
		localidade.setCep("12345-678");
		localidade.setReferencia("Próximo ao shopping");
		localidade = localidadeRepository.save(localidade);

		Cinema cinema = new Cinema();
		cinema.setNome("Cinema Central");
		cinema.setLocalidade(localidade);
		cinema = cinemaRepository.save(cinema);

		sala = new Sala();
		sala.setNumeroDaSala(1);
		sala.setCapacidade(100);
		sala.setTecnologia("IMAX");
		sala.setCinema(cinema);
		sala = salaRepository.save(sala);

		filme = new Filme();
		filme.setTitulo("Vingadores: Ultimato");
		filme.setDuracao(LocalTime.of(3, 1));
		filme.setGenero(GeneroFilme.ACAO);
		filme.setDiretor("Anthony Russo");
		filme.setValorIngresso(new BigDecimal("25.50"));
		filme.setSinopse("Os heróis restantes se unem para desfazer as ações de Thanos.");
		filme.setEmCartaz(true);
		filme = filmeRepository.save(filme);

		// Criar DTO para criação de sessão
		sessaoCreateDTO = new SessaoCreateDTO();
		sessaoCreateDTO.setDataHoraSessao(LocalDateTime.now().plusDays(1));
		sessaoCreateDTO.setSalaId(sala.getId());
		sessaoCreateDTO.setFilmeId(filme.getId());
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
	@DisplayName("Deve criar sessão com sucesso quando usuário é ADMIN")
	void deveCriarSessaoComSucesso() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(post("/api/sessoes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(sessaoCreateDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.dataHoraSessao", notNullValue()))
				.andExpect(jsonPath("$.salaId", is(sala.getId())))
				.andExpect(jsonPath("$.filmeId", is(filme.getId())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@DisplayName("Deve retornar 403 ao criar sessão sem permissão ADMIN")
	void deveRetornar403AoCriarSessaoSemPermissao() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(post("/api/sessoes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(sessaoCreateDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar todas as sessões")
	void deveListarTodasAsSessoes() throws Exception {
		Sessao sessao1 = criarSessao(LocalDateTime.now().plusDays(1), sala, filme);
		Sessao sessao2 = criarSessao(LocalDateTime.now().plusDays(2), sala, filme);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/sessoes")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(sessao1.getId())))
				.andExpect(jsonPath("$[1].id", is(sessao2.getId())));
	}

	@Test
	@DisplayName("Deve buscar sessão por ID")
	void deveBuscarSessaoPorId() throws Exception {
		Sessao sessao = criarSessao(LocalDateTime.now().plusDays(1), sala, filme);
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/sessoes/{id}", sessao.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(sessao.getId())))
				.andExpect(jsonPath("$.salaId", is(sala.getId())));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar sessão inexistente")
	void deveRetornar404AoBuscarSessaoInexistente() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/sessoes/{id}", 999)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar sessão com sucesso quando usuário é ADMIN")
	void deveAtualizarSessaoComSucesso() throws Exception {
		Sessao sessao = criarSessao(LocalDateTime.now().plusDays(1), sala, filme);
		String token = createJwtToken("admin", "ROLE_ADMIN");

		SessaoCreateDTO updateDTO = new SessaoCreateDTO();
		updateDTO.setDataHoraSessao(LocalDateTime.now().plusDays(3));
		updateDTO.setSalaId(sala.getId());
		updateDTO.setFilmeId(filme.getId());

		mockMvc.perform(put("/api/sessoes/{id}", sessao.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dataHoraSessao", containsString(updateDTO.getDataHoraSessao().toString().substring(0, 10))));
	}

	@Test
	@DisplayName("Deve deletar sessão com sucesso quando usuário é ADMIN")
	void deveDeletarSessaoComSucesso() throws Exception {
		Sessao sessao = criarSessao(LocalDateTime.now().plusDays(1), sala, filme);
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(delete("/api/sessoes/{id}", sessao.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve buscar sessões por filme")
	void deveBuscarSessoesPorFilme() throws Exception {
		Filme outroFilme = new Filme();
		outroFilme.setTitulo("Pantera Negra");
		outroFilme.setDuracao(LocalTime.of(2, 14));
		outroFilme.setGenero(GeneroFilme.ACAO);
		outroFilme.setDiretor("Ryan Coogler");
		outroFilme.setValorIngresso(new BigDecimal("24.00"));
		outroFilme.setSinopse("T'Challa retorna para Wakanda.");
		outroFilme.setEmCartaz(true);
		outroFilme = filmeRepository.save(outroFilme);

		criarSessao(LocalDateTime.now().plusDays(1), sala, filme);
		criarSessao(LocalDateTime.now().plusDays(2), sala, filme);
		criarSessao(LocalDateTime.now().plusDays(3), sala, outroFilme); // Não deve ser retornado

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/sessoes/filme/{filmeId}", filme.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].filmeId", is(filme.getId())))
				.andExpect(jsonPath("$[1].filmeId", is(filme.getId())));
	}

	@Test
	@DisplayName("Deve buscar sessões por sala")
	void deveBuscarSessoesPorSala() throws Exception {
		Sala outraSala = new Sala();
		outraSala.setNumeroDaSala(2);
		outraSala.setCapacidade(80);
		outraSala.setTecnologia("3D");
		outraSala.setCinema(sala.getCinema());
		outraSala = salaRepository.save(outraSala);

		criarSessao(LocalDateTime.now().plusDays(1), sala, filme);
		criarSessao(LocalDateTime.now().plusDays(2), sala, filme);
		criarSessao(LocalDateTime.now().plusDays(3), outraSala, filme); // Não deve ser retornado

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/sessoes/sala/{salaId}", sala.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].salaId", is(sala.getId())))
				.andExpect(jsonPath("$[1].salaId", is(sala.getId())));
	}

	@Test
	@DisplayName("Deve buscar sessões por data")
	void deveBuscarSessoesPorData() throws Exception {
		LocalDate amanha = LocalDate.now().plusDays(1);
		LocalDateTime amanhaManha = amanha.atTime(10, 0);
		LocalDateTime amanhaTarde = amanha.atTime(15, 0);
		LocalDateTime depoisAmanha = amanha.plusDays(1).atTime(10, 0);

		criarSessao(amanhaManha, sala, filme);
		criarSessao(amanhaTarde, sala, filme);
		criarSessao(depoisAmanha, sala, filme); // Não deve ser retornado

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/sessoes/data/{data}", amanha)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].dataHoraSessao", containsString(amanha.toString())))
				.andExpect(jsonPath("$[1].dataHoraSessao", containsString(amanha.toString())));
	}

	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar sessão")
	void deveValidarCamposObrigatoriosAoCriarSessao() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		SessaoCreateDTO invalidDTO = new SessaoCreateDTO(); // Campos obrigatórios ausentes

		mockMvc.perform(post("/api/sessoes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Deve retornar erro ao criar sessão com data passada")
	void deveRetornarErroAoCriarSessaoComDataPassada() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		SessaoCreateDTO invalidDTO = new SessaoCreateDTO();
		invalidDTO.setDataHoraSessao(LocalDateTime.now().minusDays(1)); // Data no passado
		invalidDTO.setSalaId(sala.getId());
		invalidDTO.setFilmeId(filme.getId());

		mockMvc.perform(post("/api/sessoes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());
	}

	private Sessao criarSessao(LocalDateTime dataHora, Sala sala, Filme filme) {
		Sessao sessao = new Sessao();
		sessao.setDataHoraSessao(dataHora);
		sessao.setSala(sala);
		sessao.setFilme(filme);
		return sessaoRepository.save(sessao);
	}
}
