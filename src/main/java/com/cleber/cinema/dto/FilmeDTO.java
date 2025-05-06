package com.cleber.cinema.dto;

import com.cleber.cinema.enums.GeneroFilme;

import java.math.BigDecimal;
import java.time.LocalTime;

public class FilmeDTO {
	private Integer id;
	private String titulo;
	private LocalTime duracao;
	private String sinopse;
	private GeneroFilme genero;
	private String diretor;
	private BigDecimal valorIngresso;
	private boolean emCartaz;
}
