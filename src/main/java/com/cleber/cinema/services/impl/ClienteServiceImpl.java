package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Cliente;
import com.cleber.cinema.repositories.ClienteRepository;
import com.cleber.cinema.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;

	@Override
	public Cliente save(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Override
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	@Override
	public Optional<Cliente> findById(Integer id) {
		return clienteRepository.findById(id);
	}

	@Override
	public void delete(Integer id) {
		clienteRepository.deleteById(id);
	}

	@Override
	public Cliente update(Integer id, Cliente clienteAtualizado) {
		return clienteRepository.findById(id)
				.map(cliente -> {
					cliente.setNome(clienteAtualizado.getNome());
					cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
					cliente.setCpf(clienteAtualizado.getCpf());
					cliente.setEmail(clienteAtualizado.getEmail());
					cliente.setSenha(clienteAtualizado.getSenha());
					cliente.setEstado(clienteAtualizado.getEstado());
					cliente.setCidade(clienteAtualizado.getCidade());
					return clienteRepository.save(cliente);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
	}

	@Override
	public Optional<Cliente> findByCpf(String cpf) {
		return clienteRepository.findByCpf(cpf);
	}

	@Override
	public Optional<Cliente> findByEmail(String email) {
		return clienteRepository.findByEmail(email);
	}

	@Override
	public boolean validacaoCliente(Cliente cliente) {
		// Implementação da lógica de validação do cliente
		if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
			return false;
		}
		if (cliente.getCpf() == null || !cliente.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			return false;
		}
		if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
			return false;
		}
		return true;
	}
}
