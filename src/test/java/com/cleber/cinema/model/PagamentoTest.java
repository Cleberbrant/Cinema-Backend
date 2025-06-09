package com.cleber.cinema.model;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {

    private Validator validator;

    private Usuario usuarioValido() {
        Localidade localidade = Localidade.builder()
                .endereco("Rua Teste")
                .cep("12345-678")
                .referencia("Perto da praça")
                .build();
        return Usuario.builder()
                .nome("Usuário")
                .dataNascimento(LocalDate.of(2000,1,1))
                .cpf("12345678900")
                .email("teste@email.com")
                .password("senha123")
                .role(com.cleber.cinema.enums.Role.ROLE_USER)
                .localidade(localidade)
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

    private Alimento alimentoValido() {
        return Alimento.builder()
                .nome("Pipoca")
                .tipo(com.cleber.cinema.enums.TipoAlimento.SALGADO)
                .preco(10.0)
                .descricao("Pipoca grande")
                .build();
    }

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void pagamentoValidoNaoDeveTerViolacoes() {
        Pagamento pagamento = Pagamento.builder()
                .numeroDoCartao("1234567890123")
                .nomeImpresso("Usuário Teste")
                .dataDeValidade("12/25")
                .codigoDeSeguranca("123")
                .valorTotal(50.0)
                .dataPagamento(LocalDateTime.now())
                .usuario(usuarioValido())
                .filme(filmeValido())
                .alimentos(List.of(alimentoValido()))
                .build();

        Set<ConstraintViolation<Pagamento>> violations = validator.validate(pagamento);
        assertTrue(violations.isEmpty());
    }

    @Test
    void pagamentoInvalidoDeveTerViolacoes() {
        Pagamento pagamento = new Pagamento();
        Set<ConstraintViolation<Pagamento>> violations = validator.validate(pagamento);
        assertFalse(violations.isEmpty());
    }
}