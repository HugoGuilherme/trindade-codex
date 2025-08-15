package br.com.trindade.project.modules.agendamento;

import java.awt.print.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trindade.project.modules.agendamento.dtos.AgendamentoLimpezaDTO;
import br.com.trindade.project.modules.agendamento.dtos.AgendamentoLimpezaFuturaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/agendamento-limpeza")
@Tag(name = "AgendamentoLimpeza", description = "Endpoints for managing AgendamentoLimpeza")
public class AgendamentoLimpezaControllers {

	@Autowired
	private AgendamentoLimpezaServices service;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds all AgendamentoLimpeza", description = "Finds all Book", tags = {
			"Book" }, responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Book.class))) }),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public Page<AgendamentoLimpeza> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@GetMapping(path = "/futuros", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds all AgendamentoLimpeza", description = "Finds all Book", tags = {
			"Book" }, responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Book.class))) }),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public Page<AgendamentoLimpezaFuturaDTO> findAgendamentosFuturos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size) {
		return service.findAgendamentosFuturos(page, size);
	}

	@GetMapping(path = "/futuros/{clientId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(
	    summary = "Busca agendamentos futuros por Cliente ID",
	    description = "Retorna uma lista paginada de agendamentos futuros para um cliente específico.",
	    tags = { "Agendamentos" },
	    responses = {
	        @ApiResponse(description = "Success", responseCode = "200", content = {
	            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AgendamentoLimpezaFuturaDTO.class)))
	        }),
	        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
	        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
	        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	    }
	)
	public ResponseEntity<Page<AgendamentoLimpezaFuturaDTO>> findAgendamentosFuturosByClienteId(
	        @PathVariable("clientId") Long clientId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "100") int size) {
	    
	    Page<AgendamentoLimpezaFuturaDTO> agendamentos = service.findAgendamentosFuturosByClienteId(clientId, page, size);
	    
	    if (agendamentos.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    
	    return ResponseEntity.ok(agendamentos);
	}


	@PostMapping
	public ResponseEntity<?> create(@RequestBody AgendamentoLimpezaDTO dto) {
		try {
			return ResponseEntity.ok(service.create(dto));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AgendamentoLimpezaDTO dto) {
		try {
			return ResponseEntity.ok(service.update(id, dto));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			service.delete(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
