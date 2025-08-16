package br.com.trindade.project.modules.tipoTarefa;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipos-tarefa")
public class TipoTarefaController {

    private final TipoTarefaService tipoTarefaService;

    public TipoTarefaController(TipoTarefaService tipoTarefaService) {
        this.tipoTarefaService = tipoTarefaService;
    }

    @GetMapping
    public ResponseEntity<List<TipoTarefa>> listarTodas() {
        return ResponseEntity.ok(tipoTarefaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoTarefa> buscarPorId(@PathVariable Long id) {
        return tipoTarefaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoTarefa> criar(@RequestBody TipoTarefa tipoTarefa) {
        return ResponseEntity.ok(tipoTarefaService.salvar(tipoTarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tipoTarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}