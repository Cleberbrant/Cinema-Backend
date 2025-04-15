package com.cleber.cinema.services;

import com.cleber.cinema.model.Cinema;

import java.util.List;

public interface CinemaService {
	Cinema save(Cinema cinema);
	List<Cinema> findAll();
	Cinema findById(Integer id);
	void delete(Integer id);
	Cinema update(Integer id, Cinema cinema);
	List<Cinema> findByNome(String nome);
}