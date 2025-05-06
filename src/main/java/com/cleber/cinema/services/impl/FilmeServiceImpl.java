package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.enums.GeneroFilme;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.services.FilmeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmeServiceImpl implements FilmeService {

	private final FilmeRepository filmeRepository;

	private FilmeDTO toDTO(Filme filme) {
		FilmeDTO dto = new FilmeDTO();
		dto.setId(filme.getId());
		dto.setTitulo(filme.getTitulo());
		dto.setDuracao(filme.getDuracao());
		dto.setSinopse(filme.getSinopse());
		dto.setGenero(filme.getGenero());
		dto.setDiretor(filme.getDiretor());
		dto.setValorIngresso(filme.getValorIngresso());
		dto.setEmCartaz(filme.isEmCartaz());
		return dto;
	}

	private Filme toEntity(FilmeDTO dto) {
		return Filme.builder()
				.id(dto.getId())
				.titulo(dto.getTitulo())
				.duracao(dto.getDuracao())
				.sinopse(dto.getSinopse())
				.genero(dto.getGenero())
				.diretor(dto.getDiretor())
				.valorIngresso(dto.getValorIngresso())
				.emCartaz(dto.isEmCartaz())
				.build();
	}

	@Override
	public FilmeDTO create(FilmeDTO dto) {
		Filme filme = toEntity(dto);
		return toDTO(filmeRepository.save(filme));
	}

	@Override
	public List<FilmeDTO> findAll() {
		return filmeRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public FilmeDTO findById(Integer id) {
		Filme filme = filmeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + id));
		return toDTO(filme);
	}

	@Override
	public FilmeDTO update(Integer id, FilmeDTO dto) {
		Filme filmeExistente = filmeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + id));
		filmeExistente.setTitulo(dto.getTitulo());
		filmeExistente.setDuracao(dto.getDuracao());
		filmeExistente.setSinopse(dto.getSinopse());
		filmeExistente.setGenero(dto.getGenero());
		filmeExistente.setDiretor(dto.getDiretor());
		filmeExistente.setValorIngresso(dto.getValorIngresso());
		filmeExistente.setEmCartaz(dto.isEmCartaz());
		return toDTO(filmeRepository.save(filmeExistente));
	}

	@Override
	public void delete(Integer id) {
		if (!filmeRepository.existsById(id)) {
			throw new ResourceNotFoundException("Filme não encontrado com id: " + id);
		}
		filmeRepository.deleteById(id);
	}

	@Override
	public List<FilmeDTO> findByEmCartaz(boolean emCartaz) {
		return filmeRepository.findByEmCartaz(emCartaz).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<FilmeDTO> findByGenero(GeneroFilme genero) {
		return filmeRepository.findByGenero(genero).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<FilmeDTO> findByTitulo(String titulo) {
		return filmeRepository.findByTituloContainingIgnoreCase(titulo).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}