package com.hugo.myapplication.pages.agenda;

public class AgendaItem {
    private String hora;
    private String titulo;
    private String descricao;

    public AgendaItem(String hora, String titulo, String descricao) {
        this.hora = hora;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getHora() { return hora; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
}