package br.com.trindade.project.modules.tarefa.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class TarefaRequestDTO {
    private String descricao;
    private LocalDateTime dataHora;
    private String status;
    private Long clienteId;
    private Long tipoTarefaId;
    private Long residenciaId; 
    private List<Long> colaboradoresIds; 
    
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
    public Long getClienteId() {
        return clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public Long getTipoTarefaId() {
        return tipoTarefaId;
    }
    public void setTipoTarefaId(Long tipoTarefaId) {
        this.tipoTarefaId = tipoTarefaId;
    }
    public Long getResidenciaId() {
        return residenciaId;
    }
    public void setResidenciaId(Long residenciaId) {
        this.residenciaId = residenciaId;
    }
    public List<Long> getColaboradoresIds() {
        return colaboradoresIds;
    }
    public void setColaboradoresIds(List<Long> colaboradoresIds) {
        this.colaboradoresIds = colaboradoresIds;
    }
    
}
