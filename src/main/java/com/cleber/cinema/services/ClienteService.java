package com.cleber.cinema.services;

import com.cleber.cinema.model.Cliente;
import com.cleber.cinema.dto.ClienteDTO;

import java.util.List;
import java.util.Optional;

public interface ClienteService extends PessoaService<Cliente> {
	Cliente createFromDTO(ClienteDTO dto);
	Cliente updateFromDTO(Integer id, ClienteDTO dto);
	boolean validacaoCliente(Cliente cliente);
}
