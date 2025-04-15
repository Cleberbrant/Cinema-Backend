package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
	List<Sala> findByCinemaId(Integer cinemaId);
	List<Sala> findByTecnologia(String tecnologia);
}