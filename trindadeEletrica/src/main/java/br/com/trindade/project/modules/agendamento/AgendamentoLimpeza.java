package br.com.trindade.project.modules.agendamento;

import java.time.LocalDate;

import br.com.trindade.project.modules.cliente.Cliente;
import br.com.trindade.project.modules.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento_limpeza")
public class AgendamentoLimpeza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String email;
	private String endereco;
	private LocalDate dataInstalacao;
	private LocalDate dataUltimaLimpeza;
	private LocalDate dataDaProximaLimpeza;
	private Integer meses;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

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

	public LocalDate getDataDaProximaLimpeza() {
		return dataDaProximaLimpeza;
	}

	public void setDataDaProximaLimpeza(LocalDate dataDaProximaLimpeza) {
		this.dataDaProximaLimpeza = dataDaProximaLimpeza;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((dataDaProximaLimpeza == null) ? 0 : dataDaProximaLimpeza.hashCode());
		result = prime * result + ((dataInstalacao == null) ? 0 : dataInstalacao.hashCode());
		result = prime * result + ((dataUltimaLimpeza == null) ? 0 : dataUltimaLimpeza.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((meses == null) ? 0 : meses.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgendamentoLimpeza other = (AgendamentoLimpeza) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (dataDaProximaLimpeza == null) {
			if (other.dataDaProximaLimpeza != null)
				return false;
		} else if (!dataDaProximaLimpeza.equals(other.dataDaProximaLimpeza))
			return false;
		if (dataInstalacao == null) {
			if (other.dataInstalacao != null)
				return false;
		} else if (!dataInstalacao.equals(other.dataInstalacao))
			return false;
		if (dataUltimaLimpeza == null) {
			if (other.dataUltimaLimpeza != null)
				return false;
		} else if (!dataUltimaLimpeza.equals(other.dataUltimaLimpeza))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (meses == null) {
			if (other.meses != null)
				return false;
		} else if (!meses.equals(other.meses))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
