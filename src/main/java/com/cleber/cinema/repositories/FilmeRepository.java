package com.cleber.cinema.repositories;

import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Integer> {
	List<Filme> findByEmCartaz(boolean emCartaz);
	List<Filme> findByGenero(GeneroFilme genero);
	List<Filme> findByTituloContainingIgnoreCase(String titulo);
}