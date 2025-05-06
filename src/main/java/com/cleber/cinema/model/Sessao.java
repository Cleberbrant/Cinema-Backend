package com.cleber.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sessao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "A data e hora da sessão são obrigatórias")
	@FutureOrPresent(message = "A data/hora da sessão deve ser no presente ou futuro")
	private LocalDateTime dataHoraSessao;

	@NotNull(message = "A sala da sessão é obrigatória")
	@ManyToOne
	@JoinColumn(name = "sala_id")
	private Sala sala;

	@NotNull(message = "O filme da sessão é obrigatório")
	@ManyToOne
	@JoinColumn(name = "filme_id")
	private Filme filme;
}