package com.cleber.cinema.services;

import com.cleber.cinema.model.Localidade;

import java.util.List;

public interface LocalidadeService {
	Localidade save(Localidade localidade);
	List<Localidade> findAll();
	Localidade findById(Integer id);
	void delete(Integer id);
	Localidade update(Integer id, Localidade localidade);
	List<Localidade> findByCep(String cep);
}
