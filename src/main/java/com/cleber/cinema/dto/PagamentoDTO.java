package com.cleber.cinema.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PagamentoDTO {
	private Integer id;
	private String numeroDoCartao;
	private String nomeImpresso;
	private String dataDeValidade;
	private String codigoDeSeguranca;
	private Double valorTotal;
	private LocalDateTime dataPagamento;
	private Long usuarioId;
	private String usuarioNome; //Nome do usuário pagador
	private Integer filmeId;
	private String filmeTitulo;
	private List<Integer> alimentosIds;
	private List<String> alimentosNomes;
}