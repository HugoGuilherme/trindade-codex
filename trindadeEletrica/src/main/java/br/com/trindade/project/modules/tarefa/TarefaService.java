package br.com.trindade.project.modules.tarefa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trindade.project.modules.agenda.Agenda;
import br.com.trindade.project.modules.agenda.AgendaRepository;
import br.com.trindade.project.modules.cliente.Cliente;
import br.com.trindade.project.modules.cliente.ClienteRepository;
import br.com.trindade.project.modules.residencia.Residencia;
import br.com.trindade.project.modules.residencia.ResidenciaRepository;
import br.com.trindade.project.modules.tarefa.dtos.TarefaRequestDTO;
import br.com.trindade.project.modules.tarefa.dtos.TarefaResponseDTO;
import br.com.trindade.project.modules.tipoTarefa.TipoTarefa;
import br.com.trindade.project.modules.tipoTarefa.TipoTarefaRepository;
import br.com.trindade.project.modules.user.User;
import br.com.trindade.project.modules.user.UserRepository;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final AgendaRepository agendaRepository;
    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final TipoTarefaRepository tipoTarefaRepository;
    private final ResidenciaRepository residenciaRepository; // 🔹 novo

    public TarefaService(TarefaRepository tarefaRepository,
            AgendaRepository agendaRepository,
            ClienteRepository clienteRepository,
            UserRepository userRepository,
            TipoTarefaRepository tipoTarefaRepository,
            ResidenciaRepository residenciaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.agendaRepository = agendaRepository;
        this.clienteRepository = clienteRepository;
        this.userRepository = userRepository;
        this.tipoTarefaRepository = tipoTarefaRepository;
        this.residenciaRepository = residenciaRepository;
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    @Transactional
    public Tarefa salvar(Long agendaId, TarefaRequestDTO dto) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Residencia residencia = residenciaRepository.findById(dto.getResidenciaId())
                .orElseThrow(() -> new RuntimeException("Residência não encontrada"));
        TipoTarefa tipo = tipoTarefaRepository.findById(dto.getTipoTarefaId())
                .orElseThrow(() -> new RuntimeException("Tipo de tarefa não encontrado"));

        // 🔹 Buscar colaboradores por ID
        List<User> colaboradores = userRepository.findAllById(dto.getColaboradoresIds());

        Tarefa tarefa = new Tarefa();
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setDataHora(dto.getDataHora());
        tarefa.setStatus(StatusTarefa.valueOf(dto.getStatus())); // cuidado aqui
        tarefa.setAgenda(agenda);
        tarefa.setCliente(cliente);
        tarefa.setResidencia(residencia);
        tarefa.setTipoTarefa(tipo);
        tarefa.setColaboradores(colaboradores);

        return tarefaRepository.save(tarefa);
    }

    public void atualizarStatus(Long tarefaId, StatusTarefa novoStatus) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefa.setStatus(novoStatus);
        tarefaRepository.save(tarefa);
    }

    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }

    public List<TarefaResponseDTO> listarPorCliente(Long clienteId) {
        return tarefaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toDTO) // converte cada Tarefa em TarefaResponseDTO
                .collect(Collectors.toList());
    }

    private TarefaResponseDTO toDTO(Tarefa tarefa) {
        TarefaResponseDTO dto = new TarefaResponseDTO();
        dto.setId(tarefa.getId());
        dto.setDescricao(tarefa.getDescricao());
        dto.setDataHora(tarefa.getDataHora());
        dto.setStatus(tarefa.getStatus().name());

        // Cliente
        dto.setClienteNome(tarefa.getCliente().getNome());

        // Residência
        dto.setResidenciaEndereco(tarefa.getResidencia().getEndereco());

        // Colaboradores (só nomes)
        dto.setColaboradoresNomes(
                tarefa.getColaboradores().stream()
                        .map(User::getFullName) // ou getUserName()
                        .collect(Collectors.toList())
        );

        return dto;
    }

}
