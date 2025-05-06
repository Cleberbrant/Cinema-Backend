package com.cleber.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cinema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O nome do cinema é obrigatório")
	@Size(max = 255, message = "O nome do cinema deve ter no máximo 255 caracteres")
	private String nome;

	@NotNull(message = "A localidade do cinema é obrigatória")
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "localidade_id")
	private Localidade localidade;

	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sala> salas = new ArrayList<>();
}