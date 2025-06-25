package com.cleber.cinema.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PagamentoCreateDTO {
	@NotBlank(message = "O número do cartão é obrigatório")
	private String numeroDoCartao;

	@NotBlank(message = "O nome impresso é obrigatório")
	private String nomeImpresso;

	@NotBlank(message = "A data de validade é obrigatória")
	private String dataDeValidade;

	@NotBlank(message = "O código de segurança é obrigatório")
	private String codigoDeSeguranca;

	@NotNull(message = "O valor total é obrigatório")
	@Positive(message = "O valor total deve ser positivo")
	private Double valorTotal;

	@NotNull(message = "O filme é obrigatório")
	private Integer filmeId;

	@NotNull(message = "Pelo menos um alimento deve ser selecionado")
	@Size(min = 1, message = "Pelo menos um alimento deve ser selecionado")
	private List<Integer> alimentosIds = new ArrayList<>();
}
