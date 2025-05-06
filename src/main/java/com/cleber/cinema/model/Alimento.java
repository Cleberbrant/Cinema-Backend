package com.cleber.cinema.model;

import com.cleber.cinema.enums.TipoAlimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O nome do alimento é obrigatório")
	private String nome;

	@NotNull(message = "O tipo do alimento é obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoAlimento tipo;

	@Positive(message = "O preço deve ser positivo")
	@NotNull(message = "O preço é obrigatório")
	private Double preco;

	private String descricao;
}