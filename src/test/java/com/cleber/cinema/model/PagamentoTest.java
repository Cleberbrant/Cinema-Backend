package com.cleber.cinema.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Métodos auxiliares para criar objetos válidos de Filme e Alimento (simplificados para teste)
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

    @Test
    void pagamentoValidoNaoDeveTerViolacoes() {
        Pagamento pagamento = Pagamento.builder()
                .numeroDoCartao("1234567890123")
                .nomeImpresso("Usuário Teste")
                .dataDeValidade("12/25")
                .codigoDeSeguranca("123")
                .valorTotal(50.0)
                .dataPagamento(LocalDateTime.now())
                .usuarioId("12345678900") // ID do usuário (CPF ou UUID)
                .filme(filmeValido())
                .alimentos(List.of(alimentoValido()))
                .build();

        Set<ConstraintViolation<Pagamento>> violations = validator.validate(pagamento);
        assertTrue(violations.isEmpty(), "Pagamento válido não deve ter violações");
    }

    @Test
    void pagamentoInvalidoDeveTerViolacoes() {
        Pagamento pagamento = new Pagamento(); // Todos os campos obrigatórios estão nulos/vazios
        Set<ConstraintViolation<Pagamento>> violations = validator.validate(pagamento);
        assertFalse(violations.isEmpty(), "Pagamento inválido deve ter violações");
    }

    @Test
    void pagamentoComUsuarioIdNuloOuVazioDeveSerInvalido() {
        // Teste com usuarioId nulo
        Pagamento pagamentoNulo = Pagamento.builder()
                .numeroDoCartao("1234567890123")
                .nomeImpresso("Usuário Teste")
                .dataDeValidade("12/25")
                .codigoDeSeguranca("123")
                .valorTotal(50.0)
                .dataPagamento(LocalDateTime.now())
                .usuarioId(null)
                .filme(filmeValido())
                .alimentos(List.of(alimentoValido()))
                .build();
        Set<ConstraintViolation<Pagamento>> violationsNulo = validator.validate(pagamentoNulo);
        assertFalse(violationsNulo.isEmpty(), "usuarioId nulo deve causar violação");

        // Teste com usuarioId vazio
        Pagamento pagamentoVazio = Pagamento.builder()
                .numeroDoCartao("1234567890123")
                .nomeImpresso("Usuário Teste")
                .dataDeValidade("12/25")
                .codigoDeSeguranca("123")
                .valorTotal(50.0)
                .dataPagamento(LocalDateTime.now())
                .usuarioId("")
                .filme(filmeValido())
                .alimentos(List.of(alimentoValido()))
                .build();
        Set<ConstraintViolation<Pagamento>> violationsVazio = validator.validate(pagamentoVazio);
        assertFalse(violationsVazio.isEmpty(), "usuarioId vazio deve causar violação");
    }

    @Test
    void pagamentoComDataPagamentoFuturaDeveSerInvalido() {
        Pagamento pagamento = Pagamento.builder()
                .numeroDoCartao("1234567890123")
                .nomeImpresso("Usuário Teste")
                .dataDeValidade("12/25")
                .codigoDeSeguranca("123")
                .valorTotal(50.0)
                .dataPagamento(LocalDateTime.now().plusDays(1)) // Data futura
                .usuarioId("12345678900")
                .filme(filmeValido())
                .alimentos(List.of(alimentoValido()))
                .build();

        Set<ConstraintViolation<Pagamento>> violations = validator.validate(pagamento);
        assertFalse(violations.isEmpty(), "Data de pagamento futura deve causar violação");
    }
}
