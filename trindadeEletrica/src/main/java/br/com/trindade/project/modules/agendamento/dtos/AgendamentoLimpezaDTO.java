package br.com.trindade.project.modules.agendamento.dtos;

import java.time.LocalDate;

public class AgendamentoLimpezaDTO {
	private String nome;
	private String email;
	private String endereco;
	private LocalDate dataInstalacao;
	private LocalDate dataUltimaLimpeza;
	private Integer meses;
	private Long userId;
	private Long clienteId;

	// Getters e Setters
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public LocalDate getDataInstalacao() {
		return dataInstalacao;
	}

	public void setDataInstalacao(LocalDate dataInstalacao) {
		this.dataInstalacao = dataInstalacao;
	}

	public LocalDate getDataUltimaLimpeza() {
		return dataUltimaLimpeza;
	}

	public void setDataUltimaLimpeza(LocalDate dataUltimaLimpeza) {
		this.dataUltimaLimpeza = dataUltimaLimpeza;
	}

	public Integer getMeses() {
		return meses;
	}

	public void setMeses(Integer meses) {
		this.meses = meses;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
}
