package com.cleber.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalaCreateDTO {
	@NotNull(message = "O número da sala é obrigatório")
	@Min(value = 1, message = "O número da sala deve ser maior que zero")
	private Integer numeroDaSala;

	@NotNull(message = "A capacidade é obrigatória")
	@Min(value = 1, message = "A capacidade deve ser maior que zero")
	private Integer capacidade;

	@NotBlank(message = "O mapa da sala é obrigatório")
	private String mapaDaSala;

	@NotBlank(message = "A tecnologia é obrigatória")
	private String tecnologia;

	@NotNull(message = "O cinema é obrigatório")
	private Integer cinemaId;
}
