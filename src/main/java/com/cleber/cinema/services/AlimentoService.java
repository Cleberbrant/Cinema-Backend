package com.cleber.cinema.services;

import com.cleber.cinema.model.Alimento;

import java.util.List;

public interface AlimentoService {
	Alimento save(Alimento alimento);
	List<Alimento> findAll();
	Alimento findById(Integer id);
	void delete(Integer id);
	Alimento update(Integer id, Alimento alimento);
	List<Alimento> findByCombo(String combo);
	List<Alimento> findByPrecoMaximo(Double preco);
	boolean selecionarLanche(Integer alimentoId);
	void escolherItem(Integer alimentoId);
	void prosseguir();
}
