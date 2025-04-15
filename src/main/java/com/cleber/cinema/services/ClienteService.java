package com.cleber.cinema.services;

import com.cleber.cinema.model.Cliente;

import java.util.Optional;

public interface ClienteService extends PessoaService<Cliente> {
	Optional<Cliente> findByCpf(String cpf);
	Optional<Cliente> findByEmail(String email);
	boolean validacaoCliente(Cliente cliente);
}
