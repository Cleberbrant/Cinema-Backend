package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Administrador extends Pessoa {

	private String endereco;

	@ManyToMany
	@JoinTable(
			name = "admin_filme",
			joinColumns = @JoinColumn(name = "admin_id"),
			inverseJoinColumns = @JoinColumn(name = "filme_id")
	)
	private List<Filme> filmes = new ArrayList<>();
}