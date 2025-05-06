package com.cleber.cinema.dto;

import lombok.Data;

@Data
public class CinemaCreateDTO {
	private String nome;
	private LocalidadeDTO localidade;
}