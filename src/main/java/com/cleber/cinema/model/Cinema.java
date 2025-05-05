package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Cinema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "localidade_id")
	private Localidade localidade;

	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
	private List<Sala> salas = new ArrayList<>();
}