package br.com.trindade.project.modules.tarefa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trindade.project.modules.agenda.Agenda;
import br.com.trindade.project.modules.agenda.AgendaRepository;
import br.com.trindade.project.modules.cliente.Cliente;
import br.com.trindade.project.modules.cliente.ClienteRepository;
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

    public TarefaService(TarefaRepository tarefaRepository,
                         AgendaRepository agendaRepository,
                         ClienteRepository clienteRepository,
                         UserRepository userRepository,
                         TipoTarefaRepository tipoTarefaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.agendaRepository = agendaRepository;
        this.clienteRepository = clienteRepository;
        this.userRepository = userRepository;
        this.tipoTarefaRepository = tipoTarefaRepository;
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    @Transactional
    public Tarefa salvar(Tarefa tarefa, Long agendaId, Long clienteId, Long colaboradorId, Long tipoTarefaId) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        User colaborador = userRepository.findById(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        TipoTarefa tipo = tipoTarefaRepository.findById(tipoTarefaId)
                .orElseThrow(() -> new RuntimeException("Tipo de tarefa não encontrado"));

        tarefa.setAgenda(agenda);
        tarefa.setCliente(cliente);
        tarefa.setColaborador(colaborador);
        tarefa.setTipoTarefa(tipo);

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
}