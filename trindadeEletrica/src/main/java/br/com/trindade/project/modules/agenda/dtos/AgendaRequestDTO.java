package br.com.trindade.project.modules.agenda.dtos;

import java.time.LocalDateTime;
import java.util.List;

import br.com.trindade.project.modules.tarefa.dtos.TarefaRequestDTO;

public class AgendaRequestDTO {
    private String titulo;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Long userId;
    private List<TarefaRequestDTO> tarefas;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDateTime getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDateTime getDataFim() {
        return dataFim;
    }
    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public List<TarefaRequestDTO> getTarefas() {
        return tarefas;
    }
    public void setTarefas(List<TarefaRequestDTO> tarefas) {
        this.tarefas = tarefas;
    }
    
    
}