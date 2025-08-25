package br.com.trindade.project.modules.tarefa;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trindade.project.modules.tarefa.dtos.TarefaRequestDTO;
import br.com.trindade.project.modules.tarefa.dtos.TarefaResponseDTO;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaMapper tarefaMapper;

    public TarefaController(TarefaService tarefaService, TarefaMapper tarefaMapper) {
        this.tarefaService = tarefaService;
        this.tarefaMapper = tarefaMapper;
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TarefaResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<TarefaResponseDTO> tarefas = tarefaService.listarPorCliente(clienteId);
        return ResponseEntity.ok(tarefas);
    }


    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(
                tarefaMapper.toDTOList(tarefaService.listarTodas())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id)
                .map(tarefa -> ResponseEntity.ok(tarefaMapper.toDTO(tarefa)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criar(
            @RequestParam Long agendaId,
            @RequestBody TarefaRequestDTO dto) {

        Tarefa tarefa = tarefaService.salvar(agendaId, dto);
        return ResponseEntity.ok(tarefaMapper.toDTO(tarefa));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestParam StatusTarefa status) {
        tarefaService.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
