package com.cleber.cinema.model;

import com.cleber.cinema.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@NotNull
	private LocalDate dataNascimento;

	@NotBlank
	@Column(unique = true)
	private String cpf;

	@NotBlank
	@Email
	@Column(unique = true)
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String endereco;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;

	@NotBlank
	private String estado;

	@NotBlank
	private String cidade;
}