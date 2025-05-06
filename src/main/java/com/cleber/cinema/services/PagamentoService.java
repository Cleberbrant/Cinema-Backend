package com.cleber.cinema.services;

import com.cleber.cinema.dto.PagamentoCreateDTO;
import com.cleber.cinema.dto.PagamentoDTO;

import java.util.List;

public interface PagamentoService {
	PagamentoDTO create(PagamentoCreateDTO dto);
	List<PagamentoDTO> findAll();
	PagamentoDTO findById(Integer id);
	PagamentoDTO update(Integer id, PagamentoCreateDTO dto);
	void delete(Integer id);
	List<PagamentoDTO> findByFilme(Integer filmeId);
}