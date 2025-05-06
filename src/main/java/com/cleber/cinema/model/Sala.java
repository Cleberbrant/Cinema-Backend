package com.cleber.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "O número da sala é obrigatório")
	@Min(value = 1, message = "O número da sala deve ser positivo")
	@Column(name = "numero_da_sala")
	private Integer numeroDaSala;

	@NotNull(message = "A capacidade é obrigatória")
	@Min(value = 1, message = "A capacidade deve ser positiva")
	private Integer capacidade;

	@Size(max = 1000, message = "O mapa da sala deve ter no máximo 1000 caracteres")
	@Column(name = "mapa_da_sala")
	private String mapaDaSala;

	@NotBlank(message = "A tecnologia é obrigatória")
	@Size(max = 100, message = "A tecnologia deve ter no máximo 100 caracteres")
	private String tecnologia;

	@NotNull(message = "O cinema é obrigatório")
	@ManyToOne
	@JoinColumn(name = "cinema_id")
	private Cinema cinema;
}