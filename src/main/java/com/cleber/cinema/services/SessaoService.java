package com.cleber.cinema.services;

import com.cleber.cinema.dto.SessaoCreateDTO;
import com.cleber.cinema.dto.SessaoDTO;

import java.time.LocalDate;
import java.util.List;

public interface SessaoService {
	SessaoDTO create(SessaoCreateDTO dto);
	List<SessaoDTO> findAll();
	SessaoDTO findById(Integer id);
	SessaoDTO update(Integer id, SessaoCreateDTO dto);
	void delete(Integer id);
	List<SessaoDTO> findByFilme(Integer filmeId);
	List<SessaoDTO> findBySala(Integer salaId);
	List<SessaoDTO> findByData(LocalDate data);
}