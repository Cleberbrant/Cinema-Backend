package com.cleber.cinema.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AdministradorDTO {
	private Integer id;
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String email;
	private String endereco;
}