package com.cleber.cinema.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoCreateDTO {
	private LocalDateTime dataHoraSessao;
	private Integer salaId;
	private Integer filmeId;
}