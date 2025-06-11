package com.cleber.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDTO {
	private Integer id;
	private String numeroDoCartao;
	private String nomeImpresso;
	private String dataDeValidade;
	private String codigoDeSeguranca;
	private Double valorTotal;
	private LocalDateTime dataPagamento;
	private String usuarioId;
	private String usuarioNome;
	private Integer filmeId;
	private String filmeTitulo;
	private List<Integer> alimentosIds;
	private List<String> alimentosNomes;
}