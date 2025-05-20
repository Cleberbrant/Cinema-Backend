package com.cleber.cinema.model;

import com.cleber.cinema.enums.GeneroFilme;
import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmeTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void filmeValidoNaoDeveTerViolacoes() {
        Filme filme = Filme.builder()
                .titulo("Matrix")
                .duracao(LocalTime.of(2, 15))
                .sinopse("Um clássico da ficção científica.")
                .genero(GeneroFilme.ACAO)
                .diretor("Lana Wachowski")
                .valorIngresso(new BigDecimal("30.00"))
                .emCartaz(true)
                .build();

        Set<ConstraintViolation<Filme>> violations = validator.validate(filme);
        assertTrue(violations.isEmpty());
    }

    @Test
    void filmeInvalidoDeveTerViolacoes() {
        Filme filme = new Filme();
        Set<ConstraintViolation<Filme>> violations = validator.validate(filme);
        assertFalse(violations.isEmpty());
    }
}