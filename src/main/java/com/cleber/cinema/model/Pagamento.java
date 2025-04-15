package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "numero_do_cartao")
	private String numeroDoCartao;

	@Column(name = "nome_impresso")
	private String nomeImpresso;

	@Column(name = "data_de_validade")
	private LocalDate dataDeValidade;

	@Column(name = "codigo_de_seguranca")
	private String codigoDeSeguranca;

	private String cpf;
	private String pix;
	private Double dinheiro;

	@ManyToOne
	@JoinColumn(name = "filme_id")
	private Filme filme;
}