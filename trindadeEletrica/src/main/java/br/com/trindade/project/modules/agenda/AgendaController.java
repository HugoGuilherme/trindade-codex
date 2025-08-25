package br.com.trindade.project.modules.agenda;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trindade.project.modules.agenda.dtos.AgendaRequestDTO;
import br.com.trindade.project.modules.agenda.dtos.AgendaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponseDTO>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        if (data != null) {
            return ResponseEntity.ok(agendaService.listarPorDataDTO(data));
        }
        return ResponseEntity.ok(agendaService.listarTodasDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        return agendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar agenda", description = "Cria uma nova agenda com tarefas")
    public ResponseEntity<Agenda> criarAgenda(@RequestBody AgendaRequestDTO dto) {
        Agenda agenda = agendaService.salvarComDTO(dto);
        return ResponseEntity.ok(agenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
