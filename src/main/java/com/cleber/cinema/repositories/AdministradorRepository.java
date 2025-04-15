package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
	Optional<Administrador> findByCpf(String cpf);
	Optional<Administrador> findByEmail(String email);
}