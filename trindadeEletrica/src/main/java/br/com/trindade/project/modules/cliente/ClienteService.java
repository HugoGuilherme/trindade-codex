package br.com.trindade.project.modules.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trindade.project.modules.cliente.dtos.ClienteIDResponseDTO;
import br.com.trindade.project.modules.cliente.dtos.ClienteListDTO;
import br.com.trindade.project.modules.cliente.dtos.ClienteResponseDTO;
import br.com.trindade.project.modules.cliente.dtos.ClienteUpdateDTO;
import br.com.trindade.project.modules.residencia.Residencia;
import br.com.trindade.project.modules.residencia.ResidenciaRepository;
import br.com.trindade.project.modules.residencia.dtos.ResidenciaDTO;
import br.com.trindade.project.modules.residencia.dtos.ResidenciaRequestDTO;
import br.com.trindade.project.modules.residencia.dtos.ResidenciaResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ResidenciaRepository residenciaRepository;

    public List<ClienteListDTO> listarClientesDTO() {
        return clienteRepository.findAll().stream()
                .map(c -> new ClienteListDTO(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.getTelefone(),
                c.getEndereco()
        ))
                .toList();
    }

    // 🔹 Buscar cliente por ID
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
    }

    public ClienteIDResponseDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));

        List<ResidenciaResponseDTO> residenciasDto = cliente.getResidencias().stream()
                .map(r -> new ResidenciaResponseDTO(
                r.getId(),
                r.getDescricao(),
                r.getEndereco(),
                r.getCidade(),
                r.getEstado(),
                r.getCep()
        ))
                .toList();

        return new ClienteIDResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getGenero(),
                cliente.getDataNascimento(),
                residenciasDto
        );
    }

    // 🔹 Criar novo cliente
    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // 🔹 Editar dados do cliente (sem mexer nas residências)
    @Transactional
    public ClienteResponseDTO editarCliente(Long id, ClienteUpdateDTO dto) {
        Cliente cliente = buscarPorId(id);

        // Atualizar dados básicos
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());
        cliente.setGenero(dto.getGenero());
        cliente.setDataNascimento(dto.getDataNascimento());

        // Atualizar residências se foram enviadas no DTO
        if (dto.getResidencias() != null) {
            Map<Long, Residencia> residenciasAtuais = cliente.getResidencias().stream()
                    .collect(Collectors.toMap(Residencia::getId, r -> r));

            List<Residencia> novasResidencias = new ArrayList<>();

            for (ResidenciaRequestDTO rDto : dto.getResidencias()) {
                if (rDto.getId() != null) {
                    Residencia existente = residenciasAtuais.get(rDto.getId());
                    if (existente != null) {
                        existente.setEndereco(rDto.getEndereco());
                        existente.setDescricao(rDto.getDescricao());
                        existente.setCidade(rDto.getCidade());
                        existente.setEstado(rDto.getEstado());
                        existente.setCep(rDto.getCep());
                        novasResidencias.add(existente);
                    } else {
                        throw new EntityNotFoundException("Residência não encontrada: " + rDto.getId());
                    }
                } else {
                    Residencia nova = new Residencia();
                    nova.setEndereco(rDto.getEndereco());
                    nova.setDescricao(rDto.getDescricao());
                    nova.setCidade(rDto.getCidade());
                    nova.setEstado(rDto.getEstado());
                    nova.setCep(rDto.getCep());
                    nova.setCliente(cliente);
                    novasResidencias.add(nova);
                }
            }

            cliente.getResidencias().clear();
            cliente.getResidencias().addAll(novasResidencias);
        }

        Cliente salvo = clienteRepository.save(cliente);

        // 🔹 Montar DTO de resposta
        List<ResidenciaResponseDTO> residenciasDto = salvo.getResidencias().stream()
                .map(r -> new ResidenciaResponseDTO(r.getId(), r.getDescricao(), r.getEndereco(), r.getCidade(), r.getEstado(), r.getCep()))
                .toList();

        return new ClienteResponseDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getTelefone(),
                salvo.getEndereco(),
                salvo.getGenero(),
                salvo.getDataNascimento(),
                residenciasDto
        );
    }

    // 🔹 Deletar cliente
    @Transactional
    public void deletarCliente(Long id) {
        Cliente cliente = buscarPorId(id);

        // ⚠️ aqui você poderia validar se o cliente tem residências/tarefas antes de excluir
        clienteRepository.delete(cliente);
    }

    // 🔹 Listar residências de um cliente
    public List<ResidenciaDTO> listarResidenciasDoCliente(Long clienteId) {
        return residenciaRepository.findByClienteId(clienteId);
    }

    // 🔹 Adicionar residência a um cliente
    @Transactional
    public Residencia adicionarResidencia(Long clienteId, Residencia residencia) {
        Cliente cliente = buscarPorId(clienteId);
        residencia.setCliente(cliente);
        return residenciaRepository.save(residencia);
    }

    // 🔹 Remover residência (caso precise)
    @Transactional
    public void removerResidencia(Long clienteId, Long residenciaId) {
        Residencia residencia = residenciaRepository.findById(residenciaId)
                .orElseThrow(() -> new EntityNotFoundException("Residência não encontrada: " + residenciaId));

        if (!residencia.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("Residência não pertence ao cliente informado");
        }

        // se quiser validar tarefas vinculadas, pode adicionar aqui
        residenciaRepository.delete(residencia);
    }

    public Residencia salvarResidencia(Residencia residencia, Long clienteId) {
        Cliente cliente = buscarPorId(clienteId);
        residencia.setCliente(cliente);
        return residenciaRepository.save(residencia);
    }
}
