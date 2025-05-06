package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.AlimentoDTO;
import com.cleber.cinema.enums.TipoAlimento;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.repositories.AlimentoRepository;
import com.cleber.cinema.services.AlimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlimentoServiceImpl implements AlimentoService {

	private final AlimentoRepository alimentoRepository;

	private AlimentoDTO toDTO(Alimento alimento) {
		AlimentoDTO dto = new AlimentoDTO();
		dto.setId(alimento.getId());
		dto.setNome(alimento.getNome());
		dto.setTipo(alimento.getTipo());
		dto.setPreco(alimento.getPreco());
		dto.setDescricao(alimento.getDescricao());
		return dto;
	}

	private Alimento toEntity(AlimentoDTO dto) {
		return Alimento.builder()
				.id(dto.getId())
				.nome(dto.getNome())
				.tipo(dto.getTipo())
				.preco(dto.getPreco())
				.descricao(dto.getDescricao())
				.build();
	}

	@Override
	public AlimentoDTO create(AlimentoDTO dto) {
		Alimento alimento = toEntity(dto);
		return toDTO(alimentoRepository.save(alimento));
	}

	@Override
	public List<AlimentoDTO> findAll() {
		return alimentoRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public AlimentoDTO findById(Integer id) {
		return alimentoRepository.findById(id)
				.map(this::toDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com id: " + id));
	}

	@Override
	public AlimentoDTO update(Integer id, AlimentoDTO dto) {
		Alimento alimento = alimentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com id: " + id));
		alimento.setNome(dto.getNome());
		alimento.setTipo(dto.getTipo());
		alimento.setPreco(dto.getPreco());
		alimento.setDescricao(dto.getDescricao());
		return toDTO(alimentoRepository.save(alimento));
	}

	@Override
	public void delete(Integer id) {
		if (!alimentoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Alimento não encontrado com id: " + id);
		}
		alimentoRepository.deleteById(id);
	}

	@Override
	public List<AlimentoDTO> findByTipo(TipoAlimento tipo) {
		return alimentoRepository.findByTipo(tipo).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<AlimentoDTO> findByNome(String nome) {
		return alimentoRepository.findByNomeContainingIgnoreCase(nome).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<AlimentoDTO> findByPrecoMaximo(Double preco) {
		return alimentoRepository.findByPrecoLessThanEqual(preco).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}