package br.com.trindade.project.modules.cliente.dtos;

public class ClienteListDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;

    public ClienteListDTO(Long id, String nome, String email, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }
}
