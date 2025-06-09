package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
	List<Pagamento> findByFilmeId(Integer filmeId);
	List<Pagamento> findByUsuarioId(String usuarioId); // Alterado para String
}