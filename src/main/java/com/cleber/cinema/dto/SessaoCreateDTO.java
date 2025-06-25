package com.cleber.cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoCreateDTO {
	@NotNull(message = "A data e hora da sessão são obrigatórias")
	@Future(message = "A data e hora da sessão deve ser futura")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataHoraSessao;

	@NotNull(message = "A sala é obrigatória")
	private Integer salaId;

	@NotNull(message = "O filme é obrigatório")
	private Integer filmeId;
}