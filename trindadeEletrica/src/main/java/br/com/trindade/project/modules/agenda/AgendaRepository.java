package br.com.trindade.project.modules.agenda;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);
}