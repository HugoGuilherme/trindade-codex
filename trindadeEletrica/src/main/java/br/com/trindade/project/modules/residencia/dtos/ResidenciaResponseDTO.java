package br.com.trindade.project.modules.residencia.dtos;

public class ResidenciaResponseDTO {
    private Long id;
    private String descricao;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;

    public ResidenciaResponseDTO(Long id, String descricao, String endereco, String cidade, String estado, String cep) {
        this.id = id;
        this.descricao = descricao; // corrigido
        this.endereco = endereco;   // correto
        this.cidade = cidade;       // correto
        this.estado = estado;       // corrigido
        this.cep = cep;             // correto
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }
}
