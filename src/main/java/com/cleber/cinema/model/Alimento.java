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
	@Size(max = 255, message = "O nome do alimento deve ter no máximo 255 caracteres")
	private String nome;

	@NotNull(message = "O tipo do alimento é obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoAlimento tipo;

	@NotNull(message = "O preço é obrigatório")
	@Positive(message = "O preço deve ser positivo")
	private Double preco;

	@Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
	private String descricao;
}