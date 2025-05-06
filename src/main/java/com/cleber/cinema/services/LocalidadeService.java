package com.cleber.cinema.services;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;

import java.util.List;

public interface LocalidadeService {
	LocalidadeDTO create(LocalidadeCreateDTO dto);
	List<LocalidadeDTO> findAll();
	LocalidadeDTO findById(Integer id);
	LocalidadeDTO update(Integer id, LocalidadeCreateDTO dto);
	void delete(Integer id);
	List<LocalidadeDTO> findByCep(String cep);
}