package com.cleber.cinema.services;

import com.cleber.cinema.model.Sessao;

import java.time.LocalDate;
import java.util.List;

public interface SessaoService {
	Sessao save(Sessao sessao);
	List<Sessao> findAll();
	Sessao findById(Integer id);
	void delete(Integer id);
	Sessao update(Integer id, Sessao sessao);
	List<Sessao> findByFilme(Integer filmeId);
	List<Sessao> findBySala(Integer salaId);
	List<Sessao> findByData(LocalDate data);
	void adicionarSessao(Sessao sessao);
	boolean selecionarSessao(Integer sessaoId);
}
