package br.com.trindade.project.modules.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trindade.project.modules.user.dtos.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Endpoints for managing User")
public class UserController {

    @Autowired
    private UserServices service;

    @PostMapping
    @Operation(summary = "Create user", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso")
    public User create(@RequestBody User user) {
        return service.create(user);
    }

@GetMapping
@Operation(summary = "List all users", description = "Lista todos os usuários")
@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
public ResponseEntity<List<UserResponseDTO>> listAll() {
    List<UserResponseDTO> users = service.findAll()
            .stream()
            .map(u -> new UserResponseDTO(u.getId(), u.getFullName())) // ✅ usa getFullName()
            .toList();

    return ResponseEntity.ok(users);
}


}
