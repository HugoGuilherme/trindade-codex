package br.com.trindade.project.modules.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trindade.project.modules.residencia.Residencia;
import br.com.trindade.project.modules.residencia.ResidenciaRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ResidenciaRepository residenciaRepository;

	public List<Cliente> listarClientes() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
	}

	public Cliente salvarCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	public Cliente editarCliente(Cliente clienteAtualizado, Long id) {
	    Cliente clienteExistente = clienteRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

	    // Atualiza os dados do cliente
	    clienteExistente.setNome(clienteAtualizado.getNome());
	    clienteExistente.setEmail(clienteAtualizado.getEmail());
	    clienteExistente.setTelefone(clienteAtualizado.getTelefone());
	    clienteExistente.setEndereco(clienteAtualizado.getEndereco());
	    clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
	    clienteExistente.setGenero(clienteAtualizado.getGenero());

	    // Retorna o cliente atualizado
	    return clienteRepository.save(clienteExistente);
	}


	public void deletarCliente(Long id) {
		clienteRepository.deleteById(id);
	}

	public List<Residencia> listarResidenciasDoCliente(Long clienteId) {
		return residenciaRepository.findByClienteId(clienteId);
	}

	public Residencia salvarResidencia(Residencia residencia, Long clienteId) {
		Cliente cliente = buscarPorId(clienteId);
		residencia.setCliente(cliente);
		return residenciaRepository.save(residencia);
	}
}
