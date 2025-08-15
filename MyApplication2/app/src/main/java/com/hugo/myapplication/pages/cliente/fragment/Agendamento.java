package com.hugo.myapplication.models;

import java.io.Serializable;

public class Agendamento implements Serializable {
    private String nome;
    private String email;
    private String endereco;
    private String dataInstalacao;
    private String dataUltimaLimpeza;
    private String dataProximaLimpeza;
    private int meses;
    private int userId;
    private Long clienteId;

    public Agendamento(String nome, String email, String endereco, String dataInstalacao,
                       String dataUltimaLimpeza, String dataProximaLimpeza, int meses, int userId, Long clienteId) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.dataInstalacao = dataInstalacao;
        this.dataUltimaLimpeza = dataUltimaLimpeza;
        this.dataProximaLimpeza = dataProximaLimpeza;
        this.meses = meses;
        this.userId = userId;
        this.clienteId = clienteId;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getDataInstalacao() {
        return dataInstalacao;
    }

    public String getDataUltimaLimpeza() {
        return dataUltimaLimpeza;
    }

    public String getDataProximaLimpeza() {
        return dataProximaLimpeza;
    }

    public int getMeses() {
        return meses;
    }

    public int getUserId() {
        return userId;
    }
    public Long getClienteId() {
        return clienteId;
    }
}
