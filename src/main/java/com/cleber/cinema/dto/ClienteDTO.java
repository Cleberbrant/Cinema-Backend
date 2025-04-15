package com.cleber.cinema.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
	private Integer id;

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	private LocalDate dataNascimento;

	@NotBlank(message = "CPF é obrigatório")
	@Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", message = "CPF deve estar no formato xxx.xxx.xxx-xx")
	private String cpf;

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email deve ser válido")
	private String email;

	@NotBlank(message = "Senha é obrigatória")
	private String senha;

	@NotBlank(message = "Estado é obrigatório")
	private String estado;

	@NotBlank(message = "Cidade é obrigatória")
	private String cidade;
}
