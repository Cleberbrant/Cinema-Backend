package com.cleber.cinema.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AdministradorCreateDTO {
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String email;
	private String senha;
	private String endereco;
}