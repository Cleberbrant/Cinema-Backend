package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "numero_da_sala")
	private Integer numeroDaSala;

	private Integer capacidade;

	@Column(name = "mapa_da_sala")
	private String mapaDaSala;

	private String tecnologia;

	@ManyToOne
	@JoinColumn(name = "cinema_id")
	private Cinema cinema;

	@OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
	private List<Sessao> sessoes = new ArrayList<>();
}