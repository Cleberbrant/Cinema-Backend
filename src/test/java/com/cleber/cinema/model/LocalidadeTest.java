package com.cleber.cinema.model;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LocalidadeTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void localidadeValidaNaoDeveTerViolacoes() {
        Localidade localidade = Localidade.builder()
                .endereco("Rua Teste")
                .cep("12345-678")
                .referencia("Perto da praça")
                .build();

        Set<ConstraintViolation<Localidade>> violations = validator.validate(localidade);
        assertTrue(violations.isEmpty());
    }

    @Test
    void localidadeInvalidaDeveTerViolacoes() {
        Localidade localidade = new Localidade();
        Set<ConstraintViolation<Localidade>> violations = validator.validate(localidade);
        assertFalse(violations.isEmpty());
    }
}