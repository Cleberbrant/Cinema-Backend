package com.cleber.cinema.model;

import com.cleber.cinema.enums.Role;
import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Validator validator;

    private Localidade localidadeValida() {
        return Localidade.builder()
                .endereco("Rua Teste")
                .cep("12345-678")
                .referencia("Referência")
                .build();
    }

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void usuarioValidoNaoDeveTerViolacoes() {
        Usuario usuario = Usuario.builder()
                .nome("Usuário Teste")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .cpf("12345678900")
                .email("teste@email.com")
                .password("senha123")
                .role(Role.ROLE_USER)
                .localidade(localidadeValida())
                .build();

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty());
    }

    @Test
    void usuarioInvalidoDeveTerViolacoes() {
        Usuario usuario = new Usuario();
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
    }
}