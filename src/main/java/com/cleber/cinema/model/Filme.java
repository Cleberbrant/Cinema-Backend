package com.cleber.cinema.model;

import com.cleber.cinema.enums.GeneroFilme;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Filme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O título é obrigatório")
	private String titulo;

	@NotNull(message = "A duração é obrigatória")
	private LocalTime duracao;

	@NotBlank(message = "A sinopse é obrigatória")
	@Column(length = 1000)
	private String sinopse;

	@NotNull(message = "O gênero é obrigatório")
	@Enumerated(EnumType.STRING)
	private GeneroFilme genero;

	@NotBlank(message = "O diretor é obrigatório")
	private String diretor;

	@NotNull(message = "O valor do ingresso é obrigatório")
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor do ingresso deve ser positivo")
	@Digits(integer = 6, fraction = 2, message = "O valor do ingresso deve ter no máximo 2 casas decimais")
	private BigDecimal valorIngresso;

	private boolean emCartaz;
}
