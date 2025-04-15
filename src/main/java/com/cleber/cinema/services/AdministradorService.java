package com.cleber.cinema.services;

import com.cleber.cinema.model.Administrador;

import java.util.Optional;

public interface AdministradorService extends PessoaService<Administrador> {
	Optional<Administrador> findByCpf(String cpf);
	Optional<Administrador> findByEmail(String email);
	boolean validacaoAdmin(Administrador admin);
}
