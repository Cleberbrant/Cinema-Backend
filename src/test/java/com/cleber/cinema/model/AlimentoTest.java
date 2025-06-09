package com.cleber.cinema.model;

import com.cleber.cinema.enums.TipoAlimento;
import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AlimentoTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void alimentoValidoNaoDeveTerViolacoes() {
        Alimento alimento = Alimento.builder()
                .nome("Pipoca")
                .tipo(TipoAlimento.SALGADO)
                .preco(10.0)
                .descricao("Pipoca grande")
                .build();

        Set<ConstraintViolation<Alimento>> violations = validator.validate(alimento);
        assertTrue(violations.isEmpty());
    }

    @Test
    void alimentoInvalidoDeveTerViolacoes() {
        Alimento alimento = new Alimento();
        Set<ConstraintViolation<Alimento>> violations = validator.validate(alimento);
        assertFalse(violations.isEmpty());
    }
}
