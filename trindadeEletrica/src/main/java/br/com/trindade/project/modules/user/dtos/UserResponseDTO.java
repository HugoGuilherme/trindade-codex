package br.com.trindade.project.modules.user.dtos;

public class UserResponseDTO {
    private Long id;
    private String fullname;

    public UserResponseDTO(Long id, String fullname) {
        this.id = id;
        this.fullname = fullname;
    }

    public Long getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }
}