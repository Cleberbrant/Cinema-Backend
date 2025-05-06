package com.cleber.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
	private String endereco;

	@NotBlank(message = "O CEP é obrigatório")
	private String cep;

	@NotBlank(message = "A referência é obrigatória")
	private String referencia;
}