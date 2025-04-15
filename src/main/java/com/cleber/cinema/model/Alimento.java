package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private String combo;
	private String pipoca;
	private String bebida;
	private String doces;
	private Double preco;
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "filme_id")
	private Filme filme;
}