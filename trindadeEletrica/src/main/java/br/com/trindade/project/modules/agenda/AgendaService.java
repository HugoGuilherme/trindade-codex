package br.com.trindade.project.modules.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.trindade.project.modules.agenda.dtos.AgendaRequestDTO;
import br.com.trindade.project.modules.agenda.dtos.AgendaResponseDTO;
import br.com.trindade.project.modules.cliente.Cliente;
import br.com.trindade.project.modules.cliente.ClienteRepository;
import br.com.trindade.project.modules.residencia.Residencia;
import br.com.trindade.project.modules.residencia.ResidenciaRepository;
import br.com.trindade.project.modules.tarefa.StatusTarefa;
import br.com.trindade.project.modules.tarefa.Tarefa;
import br.com.trindade.project.modules.tarefa.dtos.TarefaResponseDTO;
import br.com.trindade.project.modules.tipoTarefa.TipoTarefa;
import br.com.trindade.project.modules.tipoTarefa.TipoTarefaRepository;
import br.com.trindade.project.modules.user.User;
import br.com.trindade.project.modules.user.UserRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final TipoTarefaRepository tipoTarefaRepository;
    private final ResidenciaRepository residenciaRepository;

    public AgendaService(AgendaRepository agendaRepository,
            UserRepository userRepository,
            ClienteRepository clienteRepository,
            TipoTarefaRepository tipoTarefaRepository,
            ResidenciaRepository residenciaRepository) {
        this.agendaRepository = agendaRepository;
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.tipoTarefaRepository = tipoTarefaRepository;
        this.residenciaRepository = residenciaRepository;
    }

    // método novo
    public Agenda salvarComDTO(AgendaRequestDTO dto) {
        Agenda agenda = new Agenda();
        agenda.setTitulo(dto.getTitulo());
        agenda.setDescricao(dto.getDescricao());
        agenda.setDataCriacao(LocalDateTime.now());
        agenda.setDataInicio(dto.getDataInicio());
        agenda.setDataFim(dto.getDataFim());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        agenda.setUser(user);

        List<Tarefa> tarefas = dto.getTarefas().stream().map(t -> {
    Tarefa tarefa = new Tarefa();
    tarefa.setDescricao(t.getDescricao());
    tarefa.setDataHora(t.getDataHora());
    tarefa.setStatus(StatusTarefa.valueOf(t.getStatus()));

    Cliente cliente = clienteRepository.findById(t.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    tarefa.setCliente(cliente);

    // 🔹 busca vários colaboradores
    List<User> colaboradores = userRepository.findAllById(t.getColaboradoresIds());
    if (colaboradores.isEmpty()) {
        throw new RuntimeException("Nenhum colaborador encontrado para os IDs fornecidos");
    }
    tarefa.setColaboradores(colaboradores);

    TipoTarefa tipo = tipoTarefaRepository.findById(t.getTipoTarefaId())
            .orElseThrow(() -> new RuntimeException("Tipo de tarefa não encontrado"));
    tarefa.setTipoTarefa(tipo);

    if (t.getResidenciaId() != null) {
        Residencia residencia = residenciaRepository.findById(t.getResidenciaId())
                .orElseThrow(() -> new RuntimeException("Residência não encontrada"));
        tarefa.setResidencia(residencia);
    }

    tarefa.setAgenda(agenda);
    return tarefa;
}).toList();

        agenda.setTarefas(tarefas);

        return agendaRepository.save(agenda);
    }

    public List<Agenda> listarTodas() {
        return agendaRepository.findAll();
    }

    public Optional<Agenda> buscarPorId(Long id) {
        return agendaRepository.findById(id);
    }

    public Agenda salvar(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public void deletar(Long id) {
        agendaRepository.deleteById(id);
    }

    public List<AgendaResponseDTO> listarTodasDTO() {
        return agendaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> listarPorDataDTO(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay().minusSeconds(1);
        return agendaRepository.findByDataInicioBetween(inicio, fim)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AgendaResponseDTO toDTO(Agenda agenda) {
        AgendaResponseDTO dto = new AgendaResponseDTO();
        dto.setId(agenda.getId());
        dto.setTitulo(agenda.getTitulo());
        dto.setDescricao(agenda.getDescricao());
        dto.setDataInicio(agenda.getDataInicio());
        dto.setDataFim(agenda.getDataFim());

        dto.setTarefas(agenda.getTarefas().stream().map(t -> {
            TarefaResponseDTO tDto = new TarefaResponseDTO();
            tDto.setId(t.getId());
            tDto.setDescricao(t.getDescricao());
            tDto.setDataHora(t.getDataHora());
            tDto.setStatus(t.getStatus().name());
            tDto.setClienteNome(t.getCliente() != null ? t.getCliente().getNome() : null);
            tDto.setColaboradoresNomes(
                t.getColaboradores() != null
                    ? t.getColaboradores().stream()
                        .map(User::getFullName)
                        .toList()
                    : List.of()
            );
            tDto.setResidenciaEndereco(
                t.getResidencia() != null ? t.getResidencia().getEndereco() : null
            );
            tDto.setTipoTarefaNome(
                t.getTipoTarefa() != null ? t.getTipoTarefa().getNome() : null
            );


            return tDto;
        }).collect(Collectors.toList()));

        return dto;
    }
}
