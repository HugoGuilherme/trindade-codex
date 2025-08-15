package br.com.trindade.project.modules.agendamento.dtos;

import java.time.LocalDate;

public class AgendamentoLimpezaFuturaDTO {
	private Long id;
	private String nome;
	private String email;
	private String endereco;
	private LocalDate dataInstalacao;
	private LocalDate dataUltimaLimpeza;
	private LocalDate dataProximaLimpeza;
	private Integer meses;
	private Long userId;

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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
	
	public LocalDate getDataProximaLimpeza() {
		return dataProximaLimpeza;
	}

	public void setDataProximaLimpeza(LocalDate dataProximaLimpeza) {
		this.dataProximaLimpeza = dataProximaLimpeza;
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
}
