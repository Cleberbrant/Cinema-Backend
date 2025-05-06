package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.PagamentoCreateDTO;
import com.cleber.cinema.dto.PagamentoDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.model.Alimento;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.model.Pagamento;
import com.cleber.cinema.model.Usuario;
import com.cleber.cinema.repositories.AlimentoRepository;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.repositories.PagamentoRepository;
import com.cleber.cinema.repositories.UsuarioRepository;
import com.cleber.cinema.services.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoServiceImpl implements PagamentoService {

	private final PagamentoRepository pagamentoRepository;
	private final FilmeRepository filmeRepository;
	private final AlimentoRepository alimentoRepository;
	private final UsuarioRepository usuarioRepository;

	private PagamentoDTO toDTO(Pagamento pagamento) {
		PagamentoDTO dto = new PagamentoDTO();
		dto.setId(pagamento.getId());
		dto.setNumeroDoCartao(pagamento.getNumeroDoCartao());
		dto.setNomeImpresso(pagamento.getNomeImpresso());
		dto.setDataDeValidade(pagamento.getDataDeValidade());
		dto.setCodigoDeSeguranca(pagamento.getCodigoDeSeguranca());
		dto.setValorTotal(pagamento.getValorTotal());
		dto.setDataPagamento(pagamento.getDataPagamento());
		dto.setUsuarioId(pagamento.getUsuario() != null ? pagamento.getUsuario().getId() : null);
		dto.setUsuarioNome(pagamento.getUsuario() != null ? pagamento.getUsuario().getNome() : null);
		dto.setFilmeId(pagamento.getFilme() != null ? pagamento.getFilme().getId() : null);
		dto.setFilmeTitulo(pagamento.getFilme() != null ? pagamento.getFilme().getTitulo() : null);
		dto.setAlimentosIds(pagamento.getAlimentos() != null
				? pagamento.getAlimentos().stream().map(a -> a.getId()).toList()
				: null);
		dto.setAlimentosNomes(pagamento.getAlimentos() != null
				? pagamento.getAlimentos().stream().map(a -> a.getNome()).toList()
				: null);
		return dto;
	}

	private Pagamento toEntity(PagamentoCreateDTO dto) {
		Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + dto.getUsuarioId()));
		Filme filme = filmeRepository.findById(dto.getFilmeId())
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + dto.getFilmeId()));
		List<Alimento> alimentos = dto.getAlimentosIds() != null ?
				alimentoRepository.findAllById(dto.getAlimentosIds()) : null;
		return Pagamento.builder()
				.numeroDoCartao(dto.getNumeroDoCartao())
				.nomeImpresso(dto.getNomeImpresso())
				.dataDeValidade(dto.getDataDeValidade())
				.codigoDeSeguranca(dto.getCodigoDeSeguranca())
				.valorTotal(dto.getValorTotal())
				.dataPagamento(LocalDateTime.now())
				.usuario(usuario)
				.filme(filme)
				.alimentos(alimentos)
				.build();
	}

	@Override
	public PagamentoDTO create(PagamentoCreateDTO dto) {
		Pagamento pagamento = toEntity(dto);
		return toDTO(pagamentoRepository.save(pagamento));
	}

	@Override
	public List<PagamentoDTO> findAll() {
		return pagamentoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Override
	public PagamentoDTO findById(Integer id) {
		Pagamento pagamento = pagamentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com id: " + id));
		return toDTO(pagamento);
	}

	@Override
	public PagamentoDTO update(Integer id, PagamentoCreateDTO dto) {
		Pagamento pagamento = pagamentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com id: " + id));
		Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + dto.getUsuarioId()));
		Filme filme = filmeRepository.findById(dto.getFilmeId())
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + dto.getFilmeId()));
		List<Alimento> alimentos = dto.getAlimentosIds() != null ?
				alimentoRepository.findAllById(dto.getAlimentosIds()) : null;
		pagamento.setNumeroDoCartao(dto.getNumeroDoCartao());
		pagamento.setNomeImpresso(dto.getNomeImpresso());
		pagamento.setDataDeValidade(dto.getDataDeValidade());
		pagamento.setCodigoDeSeguranca(dto.getCodigoDeSeguranca());
		pagamento.setValorTotal(dto.getValorTotal());
		pagamento.setUsuario(usuario);
		pagamento.setFilme(filme);
		pagamento.setAlimentos(alimentos);
		return toDTO(pagamentoRepository.save(pagamento));
	}

	@Override
	public void delete(Integer id) {
		if (!pagamentoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Pagamento não encontrado com id: " + id);
		}
		pagamentoRepository.deleteById(id);
	}

	@Override
	public List<PagamentoDTO> findByFilme(Integer filmeId) {
		return pagamentoRepository.findByFilmeId(filmeId).stream().map(this::toDTO).collect(Collectors.toList());
	}
}