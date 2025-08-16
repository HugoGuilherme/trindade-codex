package br.com.trindade.project.modules.agenda;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public List<Agenda> listarTodas() {
        return agendaRepository.findAll();
    }

    public Optional<Agenda> buscarPorId(Long id) {
        return agendaRepository.findById(id);
    }

    public Agenda salvar(Agenda agenda) {
        agenda.setDataCriacao(java.time.LocalDateTime.now());
        return agendaRepository.save(agenda);
    }

    public void deletar(Long id) {
        agendaRepository.deleteById(id);
    }
}