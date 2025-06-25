package com.cleber.cinema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CinemaCreateDTO {
	@NotBlank(message = "O nome do cinema é obrigatório")
	private String nome;

	@NotNull(message = "A localidade do cinema é obrigatória")
	private LocalidadeDTO localidade;
}