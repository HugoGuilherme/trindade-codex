package br.com.trindade.project.modules.residencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	List<Residencia> findByClienteId(Long clienteId); // Busca todas as residências de um cliente
}
