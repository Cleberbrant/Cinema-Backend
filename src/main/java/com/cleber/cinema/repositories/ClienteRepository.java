package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	Optional<Cliente> findByCpf(String cpf);
	Optional<Cliente> findByEmail(String email);
}