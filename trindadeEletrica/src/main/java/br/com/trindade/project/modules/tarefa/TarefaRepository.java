package br.com.trindade.project.modules.tarefa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // 🔹 Busca todas as tarefas de um cliente específico
    List<Tarefa> findByClienteId(Long clienteId);
}