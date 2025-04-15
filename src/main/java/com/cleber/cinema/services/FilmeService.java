package com.cleber.cinema.services;

import com.cleber.cinema.model.Filme;

import java.util.List;

public interface FilmeService {
	Filme save(Filme filme);
	List<Filme> findAll();
	Filme findById(Integer id);
	void delete(Integer id);
	Filme update(Integer id, Filme filme);
	List<Filme> findByEmCartaz(String emCartaz);
	List<Filme> findByGenero(String genero);
	List<Filme> findByTitulo(String titulo);
	boolean confirmarFilme(Filme filme);
}