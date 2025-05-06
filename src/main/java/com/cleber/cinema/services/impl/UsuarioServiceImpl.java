package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.UsuarioDTO;
import com.cleber.cinema.model.Usuario;
import com.cleber.cinema.repositories.UsuarioRepository;
import com.cleber.cinema.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;

	@Override
	public List<UsuarioDTO> listarTodos() {
		return usuarioRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<UsuarioDTO> buscarPorId(Long id) {
		return usuarioRepository.findById(id)
				.map(this::toDTO);
	}

	@Override
	public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setRole(usuarioDTO.getRole());
		// Não atualize a senha aqui por segurança!
		usuarioRepository.save(usuario);
		return toDTO(usuario);
	}

	@Override
	public void deletar(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new RuntimeException("Usuário não encontrado");
		}
		usuarioRepository.deleteById(id);
	}

	private UsuarioDTO toDTO(Usuario usuario) {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setEmail(usuario.getEmail());
		dto.setRole(usuario.getRole());
		return dto;
	}
}