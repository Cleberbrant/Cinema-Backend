package com.cleber.cinema.services;

import com.cleber.cinema.dto.CinemaCreateDTO;
import com.cleber.cinema.dto.CinemaDTO;

import java.util.List;

public interface CinemaService {
	CinemaDTO create(CinemaCreateDTO dto);
	List<CinemaDTO> findAll();
	CinemaDTO findById(Integer id);
	CinemaDTO update(Integer id, CinemaCreateDTO dto);
	void delete(Integer id);
	List<CinemaDTO> findByNome(String nome);
}