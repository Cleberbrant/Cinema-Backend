package com.cleber.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessaoDTO {
	private Integer id;
	private LocalDateTime dataHoraSessao;
	private Integer salaId;
	private Integer filmeId;
}