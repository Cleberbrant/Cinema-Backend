package com.cleber.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaDTO {
	private Integer id;
	private Integer numeroDaSala;
	private Integer capacidade;
	private String mapaDaSala;
	private String tecnologia;
	private Integer cinemaId;
}