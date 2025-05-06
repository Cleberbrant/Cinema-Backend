package com.cleber.cinema.repositories;

import com.cleber.cinema.enums.TipoAlimento;
import com.cleber.cinema.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {
	List<Alimento> findByTipo(TipoAlimento tipo);
	List<Alimento> findByNomeContainingIgnoreCase(String nome);
	List<Alimento> findByPrecoLessThanEqual(Double preco);
}