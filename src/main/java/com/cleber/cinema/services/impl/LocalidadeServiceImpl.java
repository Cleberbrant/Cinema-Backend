package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.LocalidadeCreateDTO;
import com.cleber.cinema.dto.LocalidadeDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Localidade;
import com.cleber.cinema.repositories.LocalidadeRepository;
import com.cleber.cinema.services.LocalidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalidadeServiceImpl implements LocalidadeService {

	private final LocalidadeRepository localidadeRepository;

	private LocalidadeDTO toDTO(Localidade localidade) {
		LocalidadeDTO dto = new LocalidadeDTO();
		dto.setId(localidade.getId());
		dto.setEndereco(localidade.getEndereco());
		dto.setCep(localidade.getCep());
		dto.setReferencia(localidade.getReferencia());
		return dto;
	}

	private Localidade toEntity(LocalidadeCreateDTO dto) {
		return Localidade.builder()
				.endereco(dto.getEndereco())
				.cep(dto.getCep())
				.referencia(dto.getReferencia())
				.build();
	}

	@Override
	public LocalidadeDTO create(LocalidadeCreateDTO dto) {
		return toDTO(localidadeRepository.save(toEntity(dto)));
	}

	@Override
	public List<LocalidadeDTO> findAll() {
		return localidadeRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public LocalidadeDTO findById(Integer id) {
		return localidadeRepository.findById(id)
				.map(this::toDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Localidade não encontrada com id: " + id));
	}

	@Override
	public LocalidadeDTO update(Integer id, LocalidadeCreateDTO dto) {
		Localidade localidade = localidadeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Localidade não encontrada com id: " + id));
		localidade.setEndereco(dto.getEndereco());
		localidade.setCep(dto.getCep());
		localidade.setReferencia(dto.getReferencia());
		return toDTO(localidadeRepository.save(localidade));
	}

	@Override
	public void delete(Integer id) {
		if (!localidadeRepository.existsById(id)) {
			throw new ResourceNotFoundException("Localidade não encontrada com id: " + id);
		}
		localidadeRepository.deleteById(id);
	}

	@Override
	public List<LocalidadeDTO> findByCep(String cep) {
		return localidadeRepository.findByCepStartingWith(cep).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}