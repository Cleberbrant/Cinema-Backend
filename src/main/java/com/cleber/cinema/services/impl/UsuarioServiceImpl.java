package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.UsuarioDTO;
import com.cleber.cinema.dto.UsuarioCreateDTO;
import com.cleber.cinema.enums.Role;
import com.cleber.cinema.model.Usuario;
import com.cleber.cinema.repositories.UsuarioRepository;
import com.cleber.cinema.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

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
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setEstado(usuarioDTO.getEstado());
		usuario.setCidade(usuarioDTO.getCidade());
		usuario.setRole(usuarioDTO.getRole());
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

	@Override
	public UsuarioDTO cadastrar(UsuarioCreateDTO dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.dataNascimento(dto.getDataNascimento())
				.cpf(dto.getCpf())
				.email(dto.getEmail())
				.password(passwordEncoder.encode(dto.getPassword()))
				.endereco(dto.getEndereco())
				.estado(dto.getEstado())
				.cidade(dto.getCidade())
				.role(Role.ROLE_USER)
				.build();
		usuarioRepository.save(usuario);
		return toDTO(usuario);
	}

	@Override
	public UsuarioDTO promoverParaAdmin(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		usuario.setRole(Role.ROLE_ADMIN);
		usuarioRepository.save(usuario);
		return toDTO(usuario);
	}

	private UsuarioDTO toDTO(Usuario usuario) {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.getId());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		dto.setRole(usuario.getRole());
		dto.setEstado(usuario.getEstado());
		dto.setCidade(usuario.getCidade());
		return dto;
	}
}