package com.cleber.cinema.repositories;

import com.cleber.cinema.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
	List<Pagamento> findByFilmeId(Integer filmeId);
}
