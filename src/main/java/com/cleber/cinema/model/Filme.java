package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Filme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String titulo;
	private String duracao;
	private String sinopse;
	private String genero;
	private String emCartaz;
	private String avaliacao;
	private String diretor;
	private String ingresso;

	@ManyToMany(mappedBy = "filmes")
	private List<Cliente> clientes = new ArrayList<>();

	@ManyToMany(mappedBy = "filmes")
	private List<Administrador> administradores = new ArrayList<>();

	@OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
	private List<Alimento> alimentos = new ArrayList<>();

	@OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
	private List<Pagamento> pagamentos = new ArrayList<>();

	@OneToMany(mappedBy = "filme")
	private List<Sessao> sessoes = new ArrayList<>();
}