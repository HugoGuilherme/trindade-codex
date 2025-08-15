package br.com.trindade.project.modules.agendamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoLimpezaRepository extends JpaRepository<AgendamentoLimpeza, Long> {

    Page<AgendamentoLimpeza> findByCliente_Id(Long clientId, Pageable pageable);
}