package br.com.trindade.project.modules.tarefa.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class TarefaResponseDTO {
    private Long id;
    private String descricao;
    private LocalDateTime dataHora;
    private String status;
    private String clienteNome;
    private List<String> colaboradoresNomes;
    private String residenciaEndereco; 
    private String tipoTarefaNome; 
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getClienteNome() {
        return clienteNome;
    }
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
    
    public String getResidenciaEndereco() {
        return residenciaEndereco;
    }
    public void setResidenciaEndereco(String residenciaEndereco) {
        this.residenciaEndereco = residenciaEndereco;
    }
    public List<String> getColaboradoresNomes() {
        return colaboradoresNomes;
    }
    public void setColaboradoresNomes(List<String> colaboradoresNomes) {
        this.colaboradoresNomes = colaboradoresNomes;
    }
    public String getTipoTarefaNome() {
        return tipoTarefaNome;
    }
    public void setTipoTarefaNome(String tipoTarefaNome) {
        this.tipoTarefaNome = tipoTarefaNome;
    }    
    
}