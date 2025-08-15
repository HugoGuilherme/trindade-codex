package com.hugo.myapplication.pages.cliente.residencia;

import com.hugo.myapplication.pages.cliente.Cliente;

public class Residencia {
    private Long id;
    private String descricao;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;

    public Residencia(Long id, String descricao, String endereco, String cidade, String estado, String cep) {
        this.id = id;
        this.descricao = descricao;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // ✅ Getters e Setters
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }


}