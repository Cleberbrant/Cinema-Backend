package com.cleber.cinema.integration;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.repositories.FilmeRepository;
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
import java.time.LocalTime;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DisplayName("Filme Controller - Testes de Integração")
class FilmeControllerIntegrationTest {

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
	private FilmeRepository filmeRepository;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private FilmeDTO filmeDTO;
	private Filme filme;

	@BeforeEach
	void setUp() {
		filmeRepository.deleteAll();
		// Remova a criação do filme aqui!
		filmeDTO = new FilmeDTO();
		filmeDTO.setTitulo("Homem-Aranha: Sem Volta Para Casa");
		filmeDTO.setDuracao(LocalTime.of(2, 28));
		filmeDTO.setSinopse("Peter Parker busca a ajuda do Doutor Estranho.");
		filmeDTO.setGenero(GeneroFilme.ACAO);
		filmeDTO.setDiretor("Jon Watts");
		filmeDTO.setValorIngresso(new BigDecimal("28.00"));
		filmeDTO.setEmCartaz(true);
	}

	private Filme criarFilmeValido(String titulo, GeneroFilme genero, String diretor, BigDecimal valor, LocalTime duracao, String sinopse, boolean emCartaz) {
		Filme f = new Filme();
		f.setTitulo(titulo);
		f.setGenero(genero);
		f.setDiretor(diretor);
		f.setValorIngresso(valor);
		f.setDuracao(duracao);
		f.setSinopse(sinopse);
		f.setEmCartaz(emCartaz);
		return filmeRepository.save(f);
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
	@DisplayName("Deve criar um filme com sucesso quando usuário é ADMIN")
	void deveCriarFilmeComSucesso() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(post("/api/filmes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(filmeDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.titulo", is(filmeDTO.getTitulo())))
				.andExpect(jsonPath("$.diretor", is(filmeDTO.getDiretor())))
				.andExpect(jsonPath("$.genero", is(filmeDTO.getGenero().toString())))
				.andExpect(jsonPath("$.emCartaz", is(filmeDTO.isEmCartaz())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@DisplayName("Deve retornar 403 ao tentar criar filme sem ser ADMIN")
	void deveRetornar403AoTentarCriarFilmeSemSerAdmin() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(post("/api/filmes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(filmeDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve retornar 403 ao tentar criar filme sem token")
	void deveRetornar403AoTentarCriarFilmeSemToken() throws Exception {
		mockMvc.perform(post("/api/filmes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(filmeDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar todos os filmes")
	void deveListarTodosOsFilmes() throws Exception {
		criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		criarFilmeValido("Pantera Negra", GeneroFilme.ACAO, "Ryan Coogler", new BigDecimal("24.00"),
				LocalTime.of(2, 14), "T'Challa retorna para Wakanda.", false);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/filmes")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	@DisplayName("Deve buscar filme por ID")
	void deveBuscarFilmePorId() throws Exception {
		Filme filmeSalvo = criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/filmes/{id}", filmeSalvo.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(filmeSalvo.getId())))
				.andExpect(jsonPath("$.titulo", is(filmeSalvo.getTitulo())))
				.andExpect(jsonPath("$.diretor", is(filmeSalvo.getDiretor())));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar filme inexistente")
	void deveRetornar404AoBuscarFilmeInexistente() throws Exception {
		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/filmes/{id}", 999)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar filme com sucesso quando usuário é ADMIN")
	void deveAtualizarFilmeComSucesso() throws Exception {
		Filme filmeSalvo = criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		String token = createJwtToken("admin", "ROLE_ADMIN");

		FilmeDTO filmeAtualizado = new FilmeDTO();
		filmeAtualizado.setTitulo("Vingadores: Ultimato - Versão Estendida");
		filmeAtualizado.setDuracao(LocalTime.of(3, 30));
		filmeAtualizado.setSinopse("Versão estendida do filme.");
		filmeAtualizado.setGenero(GeneroFilme.ACAO);
		filmeAtualizado.setDiretor("Anthony e Joe Russo");
		filmeAtualizado.setValorIngresso(new BigDecimal("30.00"));
		filmeAtualizado.setEmCartaz(false);

		mockMvc.perform(put("/api/filmes/{id}", filmeSalvo.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(filmeAtualizado)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.titulo", is(filmeAtualizado.getTitulo())))
				.andExpect(jsonPath("$.diretor", is(filmeAtualizado.getDiretor())))
				.andExpect(jsonPath("$.emCartaz", is(false)));
	}

	@Test
	@DisplayName("Deve retornar 403 ao tentar atualizar filme sem ser ADMIN")
	void deveRetornar403AoTentarAtualizarFilmeSemSerAdmin() throws Exception {
		Filme filmeSalvo = criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(put("/api/filmes/{id}", filmeSalvo.getId())
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(filmeDTO)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve deletar filme com sucesso quando usuário é ADMIN")
	void deveDeletarFilmeComSucesso() throws Exception {
		Filme filmeSalvo = criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		String token = createJwtToken("admin", "ROLE_ADMIN");

		mockMvc.perform(delete("/api/filmes/{id}", filmeSalvo.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Deve retornar 403 ao tentar deletar filme sem ser ADMIN")
	void deveRetornar403AoTentarDeletarFilmeSemSerAdmin() throws Exception {
		Filme filmeSalvo = criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(delete("/api/filmes/{id}", filmeSalvo.getId())
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Deve listar filmes em cartaz")
	void deveListarFilmesEmCartaz() throws Exception {
		criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		criarFilmeValido("Filme Fora de Cartaz", GeneroFilme.DRAMA, "Diretor", new BigDecimal("20.00"),
				LocalTime.of(2, 0), "Filme que não está em cartaz", false);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/filmes/em-cartaz/{status}", true)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].emCartaz", is(true)));
	}

	@Test
	@DisplayName("Deve listar filmes por gênero")
	void deveListarFilmesPorGenero() throws Exception {
		criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		criarFilmeValido("Filme Comédia", GeneroFilme.COMEDIA, "Diretor Comédia", new BigDecimal("22.00"),
				LocalTime.of(1, 30), "Um filme de comédia", true);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/filmes/genero/{genero}", GeneroFilme.ACAO)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].genero", is("ACAO")));
	}

	@Test
	@DisplayName("Deve buscar filmes por título")
	void deveBuscarFilmesPorTitulo() throws Exception {
		criarFilmeValido("Vingadores: Ultimato", GeneroFilme.ACAO, "Anthony Russo", new BigDecimal("25.50"),
				LocalTime.of(3, 1), "Os heróis restantes se unem para desfazer as ações de Thanos.", true);

		criarFilmeValido("Vingadores: Era de Ultron", GeneroFilme.ACAO, "Joss Whedon", new BigDecimal("23.00"),
				LocalTime.of(2, 21), "Os Vingadores enfrentam Ultron", false);

		String token = createJwtToken("user", "ROLE_USER");

		mockMvc.perform(get("/api/filmes/titulo/{titulo}", "Vingadores")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].titulo", containsString("Vingadores")))
				.andExpect(jsonPath("$[1].titulo", containsString("Vingadores")));
	}

	@Test
	@DisplayName("Deve validar campos obrigatórios ao criar filme")
	void deveValidarCamposObrigatoriosAoCriarFilme() throws Exception {
		String token = createJwtToken("admin", "ROLE_ADMIN");

		FilmeDTO filmeInvalido = new FilmeDTO(); // Não define campos obrigatórios

		mockMvc.perform(post("/api/filmes")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(filmeInvalido)))
				.andExpect(status().isBadRequest());
	}
}
