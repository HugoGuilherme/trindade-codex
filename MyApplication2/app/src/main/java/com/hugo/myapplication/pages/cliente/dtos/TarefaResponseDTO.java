package com.hugo.myapplication.pages.cliente.dtos;

import java.util.List;

public class TarefaResponseDTO {
    private Long id;
    private String descricao;
    private String dataHora; // se backend retorna LocalDateTime -> String
    private String status;
    private String clienteNome;
    private List<String> colaboradoresNomes;
    private String residenciaEndereco;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public List<String> getColaboradoresNomes() { return colaboradoresNomes; }
    public void setColaboradoresNomes(List<String> colaboradoresNomes) { this.colaboradoresNomes = colaboradoresNomes; }

    public String getResidenciaEndereco() { return residenciaEndereco; }
    public void setResidenciaEndereco(String residenciaEndereco) { this.residenciaEndereco = residenciaEndereco; }
}