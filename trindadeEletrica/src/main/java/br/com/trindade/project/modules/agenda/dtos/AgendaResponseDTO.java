package br.com.trindade.project.modules.agenda.dtos;

import java.time.LocalDateTime;
import java.util.List;

import br.com.trindade.project.modules.tarefa.dtos.TarefaResponseDTO;

public class AgendaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private List<TarefaResponseDTO> tarefas;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public List<TarefaResponseDTO> getTarefas() {
        return tarefas;
    }
    public void setTarefas(List<TarefaResponseDTO> tarefas) {
        this.tarefas = tarefas;
    }

    // getters e setters
    
}