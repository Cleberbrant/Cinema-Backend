package com.cleber.cinema.dto;

import lombok.Data;
import java.util.List;

@Data
public class PagamentoCreateDTO {
	private String numeroDoCartao;
	private String nomeImpresso;
	private String dataDeValidade;
	private String codigoDeSeguranca;
	private Double valorTotal;
	private Integer filmeId;
	private List<Integer> alimentosIds;
}