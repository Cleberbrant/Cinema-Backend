package com.cleber.cinema.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoDTO {
	private Integer id;
	private LocalDateTime dataHoraSessao;
	private Integer salaId;
	private Integer filmeId;
}