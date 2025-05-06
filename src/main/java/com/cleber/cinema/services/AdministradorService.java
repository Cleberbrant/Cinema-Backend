package com.cleber.cinema.services;

import com.cleber.cinema.dto.AdministradorCreateDTO;
import com.cleber.cinema.dto.AdministradorDTO;

import java.util.List;
import java.util.Optional;

public interface AdministradorService {
	AdministradorDTO save(AdministradorCreateDTO dto);
	List<AdministradorDTO> findAll();
	Optional<AdministradorDTO> findById(Integer id);
	AdministradorDTO update(Integer id, AdministradorCreateDTO dto);
	void delete(Integer id);
	Optional<AdministradorDTO> findByCpf(String cpf);
	Optional<AdministradorDTO> findByEmail(String email);
	boolean validacaoAdmin(AdministradorCreateDTO dto);
}