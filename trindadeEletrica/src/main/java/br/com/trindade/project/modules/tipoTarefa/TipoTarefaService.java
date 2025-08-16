package br.com.trindade.project.modules.tipoTarefa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TipoTarefaService {

    private final TipoTarefaRepository tipoTarefaRepository;

    public TipoTarefaService(TipoTarefaRepository tipoTarefaRepository) {
        this.tipoTarefaRepository = tipoTarefaRepository;
    }

    public List<TipoTarefa> listarTodas() {
        return tipoTarefaRepository.findAll();
    }

    public Optional<TipoTarefa> buscarPorId(Long id) {
        return tipoTarefaRepository.findById(id);
    }

    public TipoTarefa salvar(TipoTarefa tipoTarefa) {
        return tipoTarefaRepository.save(tipoTarefa);
    }

    public void deletar(Long id) {
        tipoTarefaRepository.deleteById(id);
    }
}