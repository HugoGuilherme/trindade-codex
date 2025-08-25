package br.com.trindade.project.modules.residencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trindade.project.modules.residencia.dtos.ResidenciaDTO;

@Repository
public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	List<ResidenciaDTO> findByClienteId(Long clienteId); // Busca todas as residências de um cliente
}
