package com.cleber.cinema.model;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CinemaTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void cinemaValidoNaoDeveTerViolacoes() {
        Localidade localidade = Localidade.builder()
                .endereco("Rua Teste")
                .cep("12345-678")
                .referencia("Referência")
                .build();

        Cinema cinema = Cinema.builder()
                .nome("Cine XPTO")
                .localidade(localidade)
                .build();

        Set<ConstraintViolation<Cinema>> violations = validator.validate(cinema);
        assertTrue(violations.isEmpty());
    }

    @Test
    void cinemaInvalidoDeveTerViolacoes() {
        Cinema cinema = new Cinema();
        Set<ConstraintViolation<Cinema>> violations = validator.validate(cinema);
        assertFalse(violations.isEmpty());
    }
}