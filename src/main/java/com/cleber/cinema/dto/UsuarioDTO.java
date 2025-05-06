package com.cleber.cinema.dto;

import com.cleber.cinema.enums.Role;
import lombok.Data;

@Data
public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private Role role;
	private String estado;
	private String cidade;
}