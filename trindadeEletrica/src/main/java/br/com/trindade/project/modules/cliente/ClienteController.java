package br.com.trindade.project.modules.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trindade.project.modules.cliente.dtos.ClienteIDResponseDTO;
import br.com.trindade.project.modules.cliente.dtos.ClienteListDTO;
import br.com.trindade.project.modules.cliente.dtos.ClienteResponseDTO;
import br.com.trindade.project.modules.cliente.dtos.ClienteUpdateDTO;
import br.com.trindade.project.modules.residencia.Residencia;
import br.com.trindade.project.modules.residencia.dtos.ResidenciaDTO;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteListDTO> listarClientes() {
        return clienteService.listarClientesDTO();
    }

    @GetMapping("/{id}")
    public ClienteIDResponseDTO buscarCliente(@PathVariable Long id) {
        return clienteService.buscarClientePorId(id);
    }


    @PostMapping
    public Cliente salvarCliente(@RequestBody Cliente cliente) {
        return clienteService.salvarCliente(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> editarCliente(@PathVariable Long id,
            @RequestBody ClienteUpdateDTO dto) {
        ClienteResponseDTO response = clienteService.editarCliente(id, dto);
        return ResponseEntity.ok(response); // HTTP 200 com o DTO
    }

    @DeleteMapping("/{id}")
    public void deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
    }

    @GetMapping("/{clienteId}/residencias")
    public List<ResidenciaDTO> listarResidencias(@PathVariable Long clienteId) {
        return clienteService.listarResidenciasDoCliente(clienteId);
    }

    @PostMapping("/{clienteId}/residencias")
    public Residencia salvarResidencia(@PathVariable Long clienteId, @RequestBody Residencia residencia) {
        return clienteService.salvarResidencia(residencia, clienteId);
    }
}
