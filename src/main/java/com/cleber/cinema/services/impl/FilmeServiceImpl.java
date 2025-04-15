package com.cleber.cinema.services.impl;

import com.cleber.cinema.exception.ResourceNotFoundException;
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
}
