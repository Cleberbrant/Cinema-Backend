package com.cleber.cinema.services;

import com.cleber.cinema.dto.UsuarioDTO;
import com.cleber.cinema.dto.UsuarioCreateDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
	List<UsuarioDTO> listarTodos();
	Optional<UsuarioDTO> buscarPorId(Long id);
	UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO);
	void deletar(Long id);
	UsuarioDTO cadastrar(UsuarioCreateDTO dto);
	UsuarioDTO promoverParaAdmin(Long id);
}