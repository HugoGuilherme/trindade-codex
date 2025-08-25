package com.hugo.myapplication.pages.agenda;

import java.util.List;

public class AgendaItem {
    private String titulo;
    private String status;
    private String dataHora;
    private List<String> colaboradores;
    private String residenciaEndereco;
    private String clienteNome;
    private String tipoTarefaNome; // ✅ novo campo

    public AgendaItem(String titulo, String status, String dataHora,
                      List<String> colaboradores, String residenciaEndereco,
                      String clienteNome, String tipoTarefaNome) {
        this.titulo = titulo;
        this.status = status;
        this.dataHora = dataHora;
        this.colaboradores = colaboradores;
        this.residenciaEndereco = residenciaEndereco;
        this.clienteNome = clienteNome;
        this.tipoTarefaNome = tipoTarefaNome;
    }

    public String getTitulo() { return titulo; }
    public String getStatus() { return status; }
    public String getDataHora() { return dataHora; }
    public List<String> getColaboradores() { return colaboradores; }
    public String getResidenciaEndereco() { return residenciaEndereco; }
    public String getClienteNome() { return clienteNome; }
    public String getTipoTarefaNome() { return tipoTarefaNome; } // ✅ getter
}
