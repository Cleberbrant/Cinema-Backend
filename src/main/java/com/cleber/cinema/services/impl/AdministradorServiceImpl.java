package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Administrador;
import com.cleber.cinema.repositories.AdministradorRepository;
import com.cleber.cinema.services.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

	private final AdministradorRepository administradorRepository;

	@Override
	public Administrador save(Administrador administrador) {
		return administradorRepository.save(administrador);
	}

	@Override
	public List<Administrador> findAll() {
		return administradorRepository.findAll();
	}

	@Override
	public Optional<Administrador> findById(Integer id) {
		return administradorRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		administradorRepository.deleteById(id);
	}

	@Override
	public Administrador update(Integer id, Administrador administradorAtualizado) {
		return administradorRepository.findById(id)
				.map(administrador -> {
					administrador.setNome(administradorAtualizado.getNome());
					administrador.setDataNascimento(administradorAtualizado.getDataNascimento());
					administrador.setCpf(administradorAtualizado.getCpf());
					administrador.setEmail(administradorAtualizado.getEmail());
					administrador.setSenha(administradorAtualizado.getSenha());
					administrador.setEndereco(administradorAtualizado.getEndereco());
					return administradorRepository.save(administrador);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com id: " + id));
	}

	@Override
	public Optional<Administrador> findByCpf(String cpf) {
		return administradorRepository.findByCpf(cpf);
	}

	@Override
	public Optional<Administrador> findByEmail(String email) {
		return administradorRepository.findByEmail(email);
	}

	@Override
	public boolean validacaoAdmin(Administrador admin) {
		// Implementação da lógica de validação do administrador
		if (admin.getNome() == null || admin.getNome().isEmpty()) {
			return false;
		}
		if (admin.getCpf() == null || !admin.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			return false;
		}
		if (admin.getEmail() == null || !admin.getEmail().contains("@")) {
			return false;
		}
		if (admin.getEndereco() == null || admin.getEndereco().isEmpty()) {
			return false;
		}
		return true;
	}
}
