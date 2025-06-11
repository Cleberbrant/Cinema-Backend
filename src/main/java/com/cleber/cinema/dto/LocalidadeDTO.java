package com.cleber.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalidadeDTO {
	private Integer id;
	private String endereco;
	private String cep;
	private String referencia;
}