package com.cleber.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Localidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O endereço é obrigatório")
	@Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
	private String endereco;

	@NotBlank(message = "O CEP é obrigatório")
	@Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 00000-000")
	private String cep;

	@NotBlank(message = "A referência é obrigatória")
	@Size(max = 255, message = "A referência deve ter no máximo 255 caracteres")
	private String referencia;
}