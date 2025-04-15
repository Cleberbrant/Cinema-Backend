package com.cleber.cinema.services;

import com.cleber.cinema.model.Sala;

import java.util.List;

public interface SalaService {
	Sala save(Sala sala);
	List<Sala> findAll();
	Sala findById(Integer id);
	void delete(Integer id);
	Sala update(Integer id, Sala sala);
	List<Sala> findByCinema(Integer cinemaId);
	List<Sala> findByTecnologia(String tecnologia);
	boolean selecionarSala(Integer salaId);
	boolean selecionarTecnologia(String tecnologia);
	boolean confirmacaoSala(Sala sala);
	void criarSala(Sala sala);
}
