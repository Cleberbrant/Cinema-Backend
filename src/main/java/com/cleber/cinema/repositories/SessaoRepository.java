package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
	List<Sessao> findByFilmeId(Integer filmeId);
	List<Sessao> findBySalaId(Integer salaId);
	List<Sessao> findByDataDaSessao(LocalDate data);
}