package br.com.trindade.project.modules.cliente.dtos;

import java.time.LocalDate;
import java.util.List;

import br.com.trindade.project.modules.residencia.dtos.ResidenciaRequestDTO;

public class ClienteUpdateDTO {
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private String genero;
    private LocalDate dataNascimento;
    private List<ResidenciaRequestDTO> residencias;

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
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public List<ResidenciaRequestDTO> getResidencias() {
        return residencias;
    }
    public void setResidencias(List<ResidenciaRequestDTO> residencias) {
        this.residencias = residencias;
    }
}
