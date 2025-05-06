package com.cleber.cinema.services;

import com.cleber.cinema.dto.SalaCreateDTO;
import com.cleber.cinema.dto.SalaDTO;

import java.util.List;

public interface SalaService {
	SalaDTO create(SalaCreateDTO dto);
	List<SalaDTO> findAll();
	SalaDTO findById(Integer id);
	SalaDTO update(Integer id, SalaCreateDTO dto);
	void delete(Integer id);
	List<SalaDTO> findByCinema(Integer cinemaId);
	List<SalaDTO> findByTecnologia(String tecnologia);
}