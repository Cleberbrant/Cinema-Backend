package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
	List<Cinema> findByNomeContainingIgnoreCase(String nome);
}