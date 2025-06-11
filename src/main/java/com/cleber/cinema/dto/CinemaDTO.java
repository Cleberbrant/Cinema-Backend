package com.cleber.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDTO {
	private Integer id;
	private String nome;
	private LocalidadeDTO localidade;
}