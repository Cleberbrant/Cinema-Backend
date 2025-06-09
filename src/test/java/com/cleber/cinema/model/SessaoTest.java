package com.cleber.cinema.model;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SessaoTest {

    private Validator validator;

    private Sala salaValida() {
        Localidade localidade = Localidade.builder()
                .endereco("Rua Teste")
                .cep("12345-678")
                .referencia("Referência")
                .build();
        Cinema cinema = Cinema.builder()
                .nome("Cine XPTO")
                .localidade(localidade)
                .build();
        return Sala.builder()
                .numeroDaSala(1)
                .capacidade(100)
                .mapaDaSala("Mapa")
                .tecnologia("IMAX")
                .cinema(cinema)
                .build();
    }

    private Filme filmeValido() {
        return Filme.builder()
                .titulo("Matrix")
                .duracao(java.time.LocalTime.of(2, 15))
                .sinopse("Sinopse")
                .genero(com.cleber.cinema.enums.GeneroFilme.ACAO)
                .diretor("Diretor")
                .valorIngresso(new java.math.BigDecimal("25.00"))
                .emCartaz(true)
                .build();
    }

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void sessaoValidaNaoDeveTerViolacoes() {
        Sessao sessao = Sessao.builder()
                .dataHoraSessao(LocalDateTime.now().plusDays(1))
                .sala(salaValida())
                .filme(filmeValido())
                .build();

        Set<ConstraintViolation<Sessao>> violations = validator.validate(sessao);
        assertTrue(violations.isEmpty());
    }

    @Test
    void sessaoInvalidaDeveTerViolacoes() {
        Sessao sessao = new Sessao();
        Set<ConstraintViolation<Sessao>> violations = validator.validate(sessao);
        assertFalse(violations.isEmpty());
    }
}