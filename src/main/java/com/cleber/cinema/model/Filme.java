package com.cleber.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Filme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String titulo;
	private String duracao;
	private String sinopse;
	private String genero;
	private String avaliacao;
	private String diretor;
	private String ingresso;
	private boolean emCartaz;

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