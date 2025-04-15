package com.cleber.cinema.services;

import com.cleber.cinema.model.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaService<T extends Pessoa> {
	T save(T entity);
	List<T> findAll();
	Optional<T> findById(Integer id);
	void delete(Integer id);
	T update(Integer id, T entity);
	Optional<T> findByCpf(String cpf);
	Optional<T> findByEmail(String email);
}