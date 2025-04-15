package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Sessao;
import com.cleber.cinema.repositories.SessaoRepository;
import com.cleber.cinema.services.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

	private final SessaoRepository sessaoRepository;

	@Override
	public Sessao save(Sessao sessao) {
		return sessaoRepository.save(sessao);
	}

	@Override
	public List<Sessao> findAll() {
		return sessaoRepository.findAll();
	}

	@Override
	public Sessao findById(Integer id) {
		return sessaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		sessaoRepository.deleteById(id);
	}

	@Override
	public Sessao update(Integer id, Sessao sessaoAtualizada) {
		return sessaoRepository.findById(id)
				.map(sessao -> {
					sessao.setDataDaSessao(sessaoAtualizada.getDataDaSessao());
					sessao.setHorario(sessaoAtualizada.getHorario());
					sessao.setSala(sessaoAtualizada.getSala());
					sessao.setFilme(sessaoAtualizada.getFilme());
					return sessaoRepository.save(sessao);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com id: " + id));
	}

	@Override
	public List<Sessao> findByFilme(Integer filmeId) {
		return sessaoRepository.findByFilmeId(filmeId);
	}

	@Override
	public List<Sessao> findBySala(Integer salaId) {
		return sessaoRepository.findBySalaId(salaId);
	}

	@Override
	public List<Sessao> findByData(LocalDate data) {
		return sessaoRepository.findByDataDaSessao(data);
	}

	@Override
	public void adicionarSessao(Sessao sessao) {
		// Verificações e validações antes de adicionar a sessão
		if (sessao.getSala() == null || sessao.getFilme() == null) {
			throw new IllegalArgumentException("Sala e filme são obrigatórios para criar uma sessão");
		}

		if (sessao.getDataDaSessao() == null || sessao.getHorario() == null) {
			throw new IllegalArgumentException("Data e horário são obrigatórios para criar uma sessão");
		}

		// Verificar se já existe sessão no mesmo horário e sala
		List<Sessao> sessoesNaSala = sessaoRepository.findBySalaId(sessao.getSala().getId());
		boolean temConflito = sessoesNaSala.stream()
				.anyMatch(s -> s.getDataDaSessao().equals(sessao.getDataDaSessao())
						&& s.getHorario().equals(sessao.getHorario()));

		if (temConflito) {
			throw new IllegalStateException("Já existe uma sessão neste horário para esta sala");
		}

		sessaoRepository.save(sessao);
	}

	@Override
	public boolean selecionarSessao(Integer sessaoId) {
		return sessaoRepository.existsById(sessaoId);
	}
}
