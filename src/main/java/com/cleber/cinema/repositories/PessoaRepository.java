package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository<T extends Pessoa> extends JpaRepository<T, Integer> {
}