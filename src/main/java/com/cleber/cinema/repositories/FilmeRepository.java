package com.cleber.cinema.repositories;


import com.cleber.cinema.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Integer> {
	List<Filme> findByEmCartaz(String emCartaz);
	List<Filme> findByGenero(String genero);
	List<Filme> findByTituloContainingIgnoreCase(String titulo);
}