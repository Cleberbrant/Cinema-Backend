package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sessao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "data_da_sessao")
	private LocalDate dataDaSessao;

	private LocalTime horario;

	@ManyToOne
	@JoinColumn(name = "sala_id")
	private Sala sala;

	@ManyToOne
	@JoinColumn(name = "filme_id")
	private Filme filme;
}