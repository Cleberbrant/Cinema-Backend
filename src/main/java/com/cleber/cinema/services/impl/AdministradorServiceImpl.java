package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.AdministradorCreateDTO;
import com.cleber.cinema.dto.AdministradorDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Administrador;
import com.cleber.cinema.repositories.AdministradorRepository;
import com.cleber.cinema.services.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

	private final AdministradorRepository administradorRepository;

	private AdministradorDTO toDTO(Administrador admin) {
		AdministradorDTO dto = new AdministradorDTO();
		dto.setId(admin.getId());
		dto.setNome(admin.getNome());
		dto.setDataNascimento(admin.getDataNascimento());
		dto.setCpf(admin.getCpf());
		dto.setEmail(admin.getEmail());
		dto.setEndereco(admin.getEndereco());
		return dto;
	}

	private Administrador toEntity(AdministradorCreateDTO dto) {
		Administrador admin = new Administrador();
		admin.setNome(dto.getNome());
		admin.setDataNascimento(dto.getDataNascimento());
		admin.setCpf(dto.getCpf());
		admin.setEmail(dto.getEmail());
		admin.setSenha(dto.getSenha());
		admin.setEndereco(dto.getEndereco());
		return admin;
	}

	@Override
	public AdministradorDTO save(AdministradorCreateDTO dto) {
		Administrador admin = toEntity(dto);
		return toDTO(administradorRepository.save(admin));
	}

	@Override
	public List<AdministradorDTO> findAll() {
		return administradorRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<AdministradorDTO> findById(Integer id) {
		return administradorRepository.findById(id)
				.map(this::toDTO);
	}

	@Override
	public AdministradorDTO update(Integer id, AdministradorCreateDTO dto) {
		Administrador admin = administradorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com id: " + id));
		admin.setNome(dto.getNome());
		admin.setDataNascimento(dto.getDataNascimento());
		admin.setCpf(dto.getCpf());
		admin.setEmail(dto.getEmail());
		admin.setSenha(dto.getSenha());
		admin.setEndereco(dto.getEndereco());
		return toDTO(administradorRepository.save(admin));
	}

	@Override
	public void delete(Integer id) {
		administradorRepository.deleteById(id);
	}

	@Override
	public Optional<AdministradorDTO> findByCpf(String cpf) {
		return administradorRepository.findByCpf(cpf)
				.map(this::toDTO);
	}

	@Override
	public Optional<AdministradorDTO> findByEmail(String email) {
		return administradorRepository.findByEmail(email)
				.map(this::toDTO);
	}

	@Override
	public boolean validacaoAdmin(AdministradorCreateDTO dto) {
		if (dto.getNome() == null || dto.getNome().isEmpty()) {
			return false;
		}
		if (dto.getCpf() == null || !dto.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			return false;
		}
		if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
			return false;
		}
		if (dto.getEndereco() == null || dto.getEndereco().isEmpty()) {
			return false;
		}
		return true;
	}
}