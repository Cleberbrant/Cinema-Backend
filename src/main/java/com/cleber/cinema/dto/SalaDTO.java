package com.cleber.cinema.dto;

import lombok.Data;

@Data
public class SalaDTO {
	private Integer id;
	private Integer numeroDaSala;
	private Integer capacidade;
	private String mapaDaSala;
	private String tecnologia;
	private Integer cinemaId;
}