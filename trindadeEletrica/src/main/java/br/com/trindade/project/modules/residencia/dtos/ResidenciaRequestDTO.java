package br.com.trindade.project.modules.residencia.dtos;

public class ResidenciaRequestDTO {
    private Long id; // se vier preenchido, atualiza; se null, cria nova
    private String endereco;
    private String cidade;
    private String cep;
    private String descricao;
    private String estado;

    public ResidenciaRequestDTO() {} // Jackson precisa do construtor vazio

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}
