package com.cleber.cinema.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmeDTO {
	private Integer id;

	@NotBlank(message = "Título é obrigatório")
	private String titulo;

	@NotBlank(message = "Duração é obrigatória")
	private String duracao;

	private String sinopse;
	private String genero;
	private String emCartaz;
	private String avaliacao;
	private String diretor;
	private String ingresso;
}