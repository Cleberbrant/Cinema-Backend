package com.cleber.cinema.dto;

import com.cleber.cinema.enums.TipoAlimento;
import lombok.Data;

@Data
public class AlimentoDTO {
	private Integer id;
	private String nome;
	private TipoAlimento tipo;
	private Double preco;
	private String descricao;
}