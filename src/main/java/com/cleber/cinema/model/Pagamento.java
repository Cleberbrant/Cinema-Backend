package com.cleber.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O número do cartão é obrigatório")
	@Size(min = 13, max = 19, message = "O número do cartão deve ter entre 13 e 19 dígitos")
	@Column(name = "numero_do_cartao")
	private String numeroDoCartao;

	@NotBlank(message = "O nome impresso no cartão é obrigatório")
	@Column(name = "nome_impresso")
	private String nomeImpresso;

	@NotBlank(message = "A data de validade é obrigatória")
	@Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "A data de validade deve estar no formato MM/AA")
	@Column(name = "data_de_validade")
	private String dataDeValidade; // MM/AA

	@NotBlank(message = "O código de segurança é obrigatório")
	@Size(min = 3, max = 4, message = "O código de segurança deve ter 3 ou 4 dígitos")
	@Column(name = "codigo_de_seguranca")
	private String codigoDeSeguranca;

	@NotNull(message = "O valor total é obrigatório")
	@Positive(message = "O valor total deve ser positivo")
	@Column(name = "valor_total")
	private Double valorTotal;

	@PastOrPresent(message = "A data de pagamento não pode ser futura")
	@Column(name = "data_pagamento")
	private LocalDateTime dataPagamento;

	@NotNull(message = "O usuário é obrigatório")
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@NotNull(message = "O filme é obrigatório")
	@ManyToOne
	@JoinColumn(name = "filme_id", nullable = false)
	private Filme filme;

	@NotEmpty(message = "Pelo menos um alimento deve ser selecionado")
	@ManyToMany
	@JoinTable(
			name = "pagamento_alimento",
			joinColumns = @JoinColumn(name = "pagamento_id"),
			inverseJoinColumns = @JoinColumn(name = "alimento_id")
	)
	private List<Alimento> alimentos;
}