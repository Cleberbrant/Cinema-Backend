package com.cleber.cinema.dto;

import lombok.Data;

@Data
public class UsuarioCadastroDTO {
	private String email;
	private String password;
	private String role;
}
