package com.cleber.cinema.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
	private String email;
	private String password;
	private String role;
}
