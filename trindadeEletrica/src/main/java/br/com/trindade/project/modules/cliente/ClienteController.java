package br.com.trindade.project.modules.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trindade.project.modules.residencia.Residencia;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	public List<Cliente> listarClientes() {
		return clienteService.listarClientes();
	}

	@GetMapping("/{id}")
	public Cliente buscarCliente(@PathVariable Long id) {
		return clienteService.buscarPorId(id);
	}

	@PostMapping
	public Cliente salvarCliente(@RequestBody Cliente cliente) {
		return clienteService.salvarCliente(cliente);
	}

	@PutMapping("/{id}")
	public Cliente editarCliente(@RequestBody Cliente cliente, @PathVariable Long id) {
		return clienteService.editarCliente(cliente, id);
	}

	@DeleteMapping("/{id}")
	public void deletarCliente(@PathVariable Long id) {
		clienteService.deletarCliente(id);
	}

	@GetMapping("/{clienteId}/residencias")
	public List<Residencia> listarResidencias(@PathVariable Long clienteId) {
		return clienteService.listarResidenciasDoCliente(clienteId);
	}

	@PostMapping("/{clienteId}/residencias")
	public Residencia salvarResidencia(@PathVariable Long clienteId, @RequestBody Residencia residencia) {
		return clienteService.salvarResidencia(residencia, clienteId);
	}
}
