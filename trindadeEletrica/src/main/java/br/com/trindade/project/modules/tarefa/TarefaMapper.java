package br.com.trindade.project.modules.tarefa;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.trindade.project.modules.tarefa.dtos.TarefaResponseDTO;
import br.com.trindade.project.modules.user.User;

@Component
public class TarefaMapper {

    public TarefaResponseDTO toDTO(Tarefa tarefa) {
        TarefaResponseDTO dto = new TarefaResponseDTO();
        dto.setId(tarefa.getId());
        dto.setDescricao(tarefa.getDescricao());
        dto.setDataHora(tarefa.getDataHora());
        dto.setStatus(tarefa.getStatus().name());
        dto.setClienteNome(tarefa.getCliente().getNome());
        dto.setResidenciaEndereco(tarefa.getResidencia().getEndereco());
        dto.setColaboradoresNomes(
                tarefa.getColaboradores().stream()
                        .map(User::getFullName)
                        .toList()
        );
        return dto;
    }

    public List<TarefaResponseDTO> toDTOList(List<Tarefa> tarefas) {
        return tarefas.stream().map(this::toDTO).toList();
    }
}
