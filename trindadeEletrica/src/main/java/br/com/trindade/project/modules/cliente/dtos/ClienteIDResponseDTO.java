package br.com.trindade.project.modules.cliente.dtos;

import java.time.LocalDate;
import java.util.List;

import br.com.trindade.project.modules.residencia.dtos.ResidenciaResponseDTO;

public class ClienteIDResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private String genero;
    private LocalDate dataNascimento;
    private List<ResidenciaResponseDTO> residencias;

    public ClienteIDResponseDTO(Long id, String nome, String email, String telefone,
                                String endereco, String genero, LocalDate dataNascimento,
                                List<ResidenciaResponseDTO> residencias) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.residencias = residencias;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public String getGenero() { return genero; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public List<ResidenciaResponseDTO> getResidencias() { return residencias; }
}
