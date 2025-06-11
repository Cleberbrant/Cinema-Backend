package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.SessaoCreateDTO;
import com.cleber.cinema.dto.SessaoDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.model.Sala;
import com.cleber.cinema.model.Sessao;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.repositories.SalaRepository;
import com.cleber.cinema.repositories.SessaoRepository;
import com.cleber.cinema.services.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

	private final SessaoRepository sessaoRepository;
	private final SalaRepository salaRepository;
	private final FilmeRepository filmeRepository;

	private SessaoDTO toDTO(Sessao sessao) {
		SessaoDTO dto = new SessaoDTO();
		dto.setId(sessao.getId());
		dto.setDataHoraSessao(sessao.getDataHoraSessao());
		if (sessao.getSala() == null) {
			throw new IllegalStateException("Sessão sem sala associada");
		}
		dto.setSalaId(sessao.getSala().getId());
		if (sessao.getFilme() == null) {
			throw new IllegalStateException("Sessão sem filme associado");
		}
		dto.setFilmeId(sessao.getFilme().getId());
		return dto;
	}


	private Sessao toEntity(SessaoCreateDTO dto) {
		Sala sala = salaRepository.findById(dto.getSalaId())
				.orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com id: " + dto.getSalaId()));
		Filme filme = filmeRepository.findById(dto.getFilmeId())
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + dto.getFilmeId()));
		return Sessao.builder()
				.dataHoraSessao(dto.getDataHoraSessao())
				.sala(sala)
				.filme(filme)
				.build();
	}

	@Override
	public SessaoDTO create(SessaoCreateDTO dto) {
		Sessao sessao = toEntity(dto);
		return toDTO(sessaoRepository.save(sessao));
	}

	@Override
	public List<SessaoDTO> findAll() {
		return sessaoRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public SessaoDTO findById(Integer id) {
		Sessao sessao = sessaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com id: " + id));
		return toDTO(sessao);
	}

	@Override
	public SessaoDTO update(Integer id, SessaoCreateDTO dto) {
		Sessao sessao = sessaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com id: " + id));
		Sala sala = salaRepository.findById(dto.getSalaId())
				.orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com id: " + dto.getSalaId()));
		Filme filme = filmeRepository.findById(dto.getFilmeId())
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + dto.getFilmeId()));
		sessao.setDataHoraSessao(dto.getDataHoraSessao());
		sessao.setSala(sala);
		sessao.setFilme(filme);
		return toDTO(sessaoRepository.save(sessao));
	}

	@Override
	public void delete(Integer id) {
		if (!sessaoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Sessão não encontrada com id: " + id);
		}
		sessaoRepository.deleteById(id);
	}

	@Override
	public List<SessaoDTO> findByFilme(Integer filmeId) {
		return sessaoRepository.findByFilmeId(filmeId).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<SessaoDTO> findBySala(Integer salaId) {
		return sessaoRepository.findBySalaId(salaId).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<SessaoDTO> findByData(LocalDate data) {
		LocalDateTime start = data.atStartOfDay();
		LocalDateTime end = data.atTime(LocalTime.MAX);
		return sessaoRepository.findByDataHoraSessaoBetween(start, end).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}