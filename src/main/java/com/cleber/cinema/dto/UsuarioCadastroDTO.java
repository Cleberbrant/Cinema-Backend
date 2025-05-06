package com.cleber.cinema.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioCadastroDTO {
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String email;
	private String password;
	private String endereco;
	private String estado;
	private String cidade;
	private String role; // Opcional, pode ser null para default ROLE_USER
}