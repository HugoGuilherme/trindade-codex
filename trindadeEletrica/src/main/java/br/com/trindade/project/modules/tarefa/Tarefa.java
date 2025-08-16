package br.com.trindade.project.modules.tarefa;


import java.time.LocalDateTime;

import br.com.trindade.project.modules.agenda.Agenda;
import br.com.trindade.project.modules.cliente.Cliente;
import br.com.trindade.project.modules.tipoTarefa.TipoTarefa;
import br.com.trindade.project.modules.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private User colaborador;

    @ManyToOne
    @JoinColumn(name = "tipo_tarefa_id", nullable = false)
    private TipoTarefa tipoTarefa;

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

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public User getColaborador() {
        return colaborador;
    }

    public void setColaborador(User colaborador) {
        this.colaborador = colaborador;
    }

    public TipoTarefa getTipoTarefa() {
        return tipoTarefa;
    }

    public void setTipoTarefa(TipoTarefa tipoTarefa) {
        this.tipoTarefa = tipoTarefa;
    }

    // Getters e Setters
    
}