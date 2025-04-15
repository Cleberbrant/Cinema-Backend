package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Localidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalidadeRepository extends JpaRepository<Localidade, Integer> {
	List<Localidade> findByCepStartingWith(String cep);
}