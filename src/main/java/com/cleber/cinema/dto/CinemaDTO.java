package com.cleber.cinema.dto;

import lombok.Data;

@Data
public class CinemaDTO {
	private Integer id;
	private String nome;
	private LocalidadeDTO localidade;
	// Não inclua lista de salas para simplificar a resposta
}