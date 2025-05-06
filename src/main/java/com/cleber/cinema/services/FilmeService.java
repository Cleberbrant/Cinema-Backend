package com.cleber.cinema.services;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.enums.GeneroFilme;

import java.util.List;

public interface FilmeService {
	FilmeDTO create(FilmeDTO dto);
	List<FilmeDTO> findAll();
	FilmeDTO findById(Integer id);
	FilmeDTO update(Integer id, FilmeDTO dto);
	void delete(Integer id);
	List<FilmeDTO> findByEmCartaz(boolean emCartaz);
	List<FilmeDTO> findByGenero(GeneroFilme genero);
	List<FilmeDTO> findByTitulo(String titulo);
}