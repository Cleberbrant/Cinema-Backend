package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {
	List<Alimento> findByCombo(String combo);
	List<Alimento> findByPrecoLessThanEqual(Double preco);
}