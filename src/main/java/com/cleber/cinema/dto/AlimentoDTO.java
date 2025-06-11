package com.cleber.cinema.dto;

import com.cleber.cinema.enums.TipoAlimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlimentoDTO {
	private Integer id;
	private String nome;
	private TipoAlimento tipo;
	private Double preco;
	private String descricao;
}