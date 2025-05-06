package com.cleber.cinema.services;

import com.cleber.cinema.dto.AlimentoDTO;
import com.cleber.cinema.enums.TipoAlimento;

import java.util.List;

public interface AlimentoService {
	AlimentoDTO create(AlimentoDTO dto);
	List<AlimentoDTO> findAll();
	AlimentoDTO findById(Integer id);
	AlimentoDTO update(Integer id, AlimentoDTO dto);
	void delete(Integer id);
	List<AlimentoDTO> findByTipo(TipoAlimento tipo);
	List<AlimentoDTO> findByNome(String nome);
	List<AlimentoDTO> findByPrecoMaximo(Double preco);
}