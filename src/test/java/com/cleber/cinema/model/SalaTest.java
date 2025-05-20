package com.cleber.cinema.model;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SalaTest {

    private Validator validator;

    private Cinema cinemaValido() {
        Localidade localidade = Localidade.builder()
                .endereco("Rua Teste")
                .cep("12345-678")
                .referencia("Referência")
                .build();
        return Cinema.builder()
                .nome("Cine XPTO")
                .localidade(localidade)
                .build();
    }

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void salaValidaNaoDeveTerViolacoes() {
        Sala sala = Sala.builder()
                .numeroDaSala(1)
                .capacidade(100)
                .mapaDaSala("Mapa")
                .tecnologia("IMAX")
                .cinema(cinemaValido())
                .build();

        Set<ConstraintViolation<Sala>> violations = validator.validate(sala);
        assertTrue(violations.isEmpty());
    }

    @Test
    void salaInvalidaDeveTerViolacoes() {
        Sala sala = new Sala();
        Set<ConstraintViolation<Sala>> violations = validator.validate(sala);
        assertFalse(violations.isEmpty());
    }
}