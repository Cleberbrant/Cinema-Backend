package com.cleber.cinema.services.impl;

import com.cleber.cinema.dto.FilmeDTO;
import com.cleber.cinema.exception.ResourceNotFoundException;
import com.cleber.cinema.exception.ValidationException;
import com.cleber.cinema.model.Filme;
import com.cleber.cinema.repositories.FilmeRepository;
import com.cleber.cinema.services.FilmeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmeServiceImpl implements FilmeService {

	private final FilmeRepository filmeRepository;

	@Override
	public Filme save(Filme filme) {
		return filmeRepository.save(filme);
	}

	@Override
	public List<Filme> findAll() {
		return filmeRepository.findAll();
	}

	@Override
	public Filme findById(Integer id) {
		return filmeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + id));
	}

	@Override
	public void delete(Integer id) {
		filmeRepository.deleteById(id);
	}

	@Override
	public Filme update(Integer id, Filme filmeAtualizado) {
		return filmeRepository.findById(id)
				.map(filme -> {
					filme.setTitulo(filmeAtualizado.getTitulo());
					filme.setDuracao(filmeAtualizado.getDuracao());
					filme.setSinopse(filmeAtualizado.getSinopse());
					filme.setGenero(filmeAtualizado.getGenero());
					filme.setEmCartaz(filmeAtualizado.getEmCartaz());
					filme.setAvaliacao(filmeAtualizado.getAvaliacao());
					filme.setDiretor(filmeAtualizado.getDiretor());
					filme.setIngresso(filmeAtualizado.getIngresso());
					return filmeRepository.save(filme);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + id));
	}

	@Override
	public List<Filme> findByEmCartaz(String emCartaz) {
		return filmeRepository.findByEmCartaz(emCartaz);
	}

	@Override
	public List<Filme> findByGenero(String genero) {
		return filmeRepository.findByGenero(genero);
	}

	@Override
	public List<Filme> findByTitulo(String titulo) {
		return filmeRepository.findByTituloContainingIgnoreCase(titulo);
	}

	@Override
	public boolean confirmarFilme(Filme filme) {
		// Implementação da lógica de confirmação do filme
		if (filme.getTitulo() == null || filme.getTitulo().isEmpty()) {
			return false;
		}
		if (filme.getDuracao() == null || filme.getDuracao().isEmpty()) {
			return false;
		}
		if (filme.getSinopse() == null || filme.getSinopse().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public Filme createFromDTO(FilmeDTO dto) {
		// Validar os dados do DTO
		if (dto.getTitulo() == null || dto.getTitulo().isEmpty()) {
			throw new ValidationException("O título do filme é obrigatório");
		}

		// Criar uma nova entidade a partir do DTO
		Filme novoFilme = Filme.builder()
				.titulo(dto.getTitulo())
				.duracao(dto.getDuracao())
				.sinopse(dto.getSinopse())
				.genero(dto.getGenero())
				.emCartaz(dto.getEmCartaz())
				.avaliacao(dto.getAvaliacao())
				.diretor(dto.getDiretor())
				.ingresso(dto.getIngresso())
				.build();

		// Salvar e retornar a nova entidade
		return filmeRepository.save(novoFilme);
	}

	@Override
	public Filme updateFromDTO(Integer id, FilmeDTO dto) {
		// Recuperar o filme existente
		Filme filmeExistente = filmeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id: " + id));

		// Atualizar os campos com os dados do DTO
		filmeExistente.setTitulo(dto.getTitulo());
		filmeExistente.setDuracao(dto.getDuracao());
		filmeExistente.setSinopse(dto.getSinopse());
		filmeExistente.setGenero(dto.getGenero());
		filmeExistente.setEmCartaz(dto.getEmCartaz());
		filmeExistente.setAvaliacao(dto.getAvaliacao());
		filmeExistente.setDiretor(dto.getDiretor());
		filmeExistente.setIngresso(dto.getIngresso());

		// Validar os dados atualizados
		if (!confirmarFilme(filmeExistente)) {
			throw new ValidationException("Dados do filme inválidos");
		}

		// Salvar e retornar a entidade atualizada
		return filmeRepository.save(filmeExistente);
	}
}
