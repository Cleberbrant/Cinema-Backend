package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.ClienteDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.exception.ValidationException;
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
		if (!validacaoCliente(cliente)) {
			throw new ValidationException("Dados do cliente inválidos");
		}
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
		if (!clienteRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cliente não encontrado com id: " + id);
		}
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

					if (!validacaoCliente(cliente)) {
						throw new ValidationException("Dados do cliente inválidos");
					}

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
	public Cliente createFromDTO(ClienteDTO dto) {
		// Verificar se CPF já existe
		clienteRepository.findByCpf(dto.getCpf()).ifPresent(c -> {
			throw new ValidationException("CPF já cadastrado");
		});

		// Verificar se email já existe
		clienteRepository.findByEmail(dto.getEmail()).ifPresent(c -> {
			throw new ValidationException("Email já cadastrado");
		});

		Cliente cliente = Cliente.builder()
				.nome(dto.getNome())
				.dataNascimento(dto.getDataNascimento())
				.cpf(dto.getCpf())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.estado(dto.getEstado())
				.cidade(dto.getCidade())
				.build();

		return save(cliente);
	}

	@Override
	public Cliente updateFromDTO(Integer id, ClienteDTO dto) {
		Cliente clienteExistente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));

		// Verificar se CPF já existe e não pertence a este cliente
		clienteRepository.findByCpf(dto.getCpf())
				.filter(c -> !c.getId().equals(id))
				.ifPresent(c -> {
					throw new ValidationException("CPF já cadastrado para outro cliente");
				});

		// Verificar se email já existe e não pertence a este cliente
		clienteRepository.findByEmail(dto.getEmail())
				.filter(c -> !c.getId().equals(id))
				.ifPresent(c -> {
					throw new ValidationException("Email já cadastrado para outro cliente");
				});

		clienteExistente.setNome(dto.getNome());
		clienteExistente.setDataNascimento(dto.getDataNascimento());
		clienteExistente.setCpf(dto.getCpf());
		clienteExistente.setEmail(dto.getEmail());
		clienteExistente.setSenha(dto.getSenha());
		clienteExistente.setEstado(dto.getEstado());
		clienteExistente.setCidade(dto.getCidade());

		return update(id, clienteExistente);
	}

	@Override
	public boolean validacaoCliente(Cliente cliente) {
		if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
			return false;
		}

		if (cliente.getCpf() == null || !cliente.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			return false;
		}

		if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
			return false;
		}

		if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
			return false;
		}

		if (cliente.getEstado() == null || cliente.getEstado().trim().isEmpty()) {
			return false;
		}

		if (cliente.getCidade() == null || cliente.getCidade().trim().isEmpty()) {
			return false;
		}

		return true;
	}
}
