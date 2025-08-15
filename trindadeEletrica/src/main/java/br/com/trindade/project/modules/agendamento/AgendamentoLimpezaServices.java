package br.com.trindade.project.modules.agendamento;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.trindade.project.modules.cliente.Cliente;
import br.com.trindade.project.modules.cliente.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.trindade.project.modules.agendamento.dtos.AgendamentoLimpezaDTO;
import br.com.trindade.project.modules.agendamento.dtos.AgendamentoLimpezaFuturaDTO;
import br.com.trindade.project.modules.user.User;
import br.com.trindade.project.modules.user.UserRepository;

@Service
public class AgendamentoLimpezaServices {

	@Autowired
	AgendamentoLimpezaRepository repository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ClienteRepository clienteRepository;

	public Page<AgendamentoLimpeza> findAll(Pageable pageable) {

		var agendamentos = repository.findAll(pageable);

		return agendamentos;
	}

	public Page<AgendamentoLimpezaFuturaDTO> findAgendamentosFuturos(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<AgendamentoLimpeza> agendamentos = repository.findAll(pageable);

		List<AgendamentoLimpezaFuturaDTO> listaOrdenada = agendamentos.stream().map(agendamento -> {
			AgendamentoLimpezaFuturaDTO dto = new AgendamentoLimpezaFuturaDTO();
			dto.setId(agendamento.getId());
			dto.setNome(agendamento.getNome());
			dto.setEmail(agendamento.getEmail());
			dto.setEndereco(agendamento.getEndereco());
			dto.setDataInstalacao(agendamento.getDataInstalacao());
			dto.setDataUltimaLimpeza(agendamento.getDataUltimaLimpeza());
			dto.setMeses(agendamento.getMeses());
			dto.setUserId(agendamento.getUser().getId());

			// Define a próxima limpeza corretamente
			dto.setDataProximaLimpeza(agendamento.getDataUltimaLimpeza().plusMonths(agendamento.getMeses()));

			return dto;
		}).sorted(Comparator.comparing(AgendamentoLimpezaFuturaDTO::getDataProximaLimpeza))
				.collect(Collectors.toList());

		return new PageImpl<>(listaOrdenada, pageable, agendamentos.getTotalElements());
	}

	public Page<AgendamentoLimpezaFuturaDTO> findAgendamentosFuturosByClienteId(Long clientId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<AgendamentoLimpeza> agendamentos = repository.findByCliente_Id(clientId, pageable);

		List<AgendamentoLimpezaFuturaDTO> listaOrdenada = agendamentos.stream().map(agendamento -> {
			AgendamentoLimpezaFuturaDTO dto = new AgendamentoLimpezaFuturaDTO();
			dto.setNome(agendamento.getNome());
			dto.setEmail(agendamento.getEmail());
			dto.setEndereco(agendamento.getEndereco());
			dto.setDataInstalacao(agendamento.getDataInstalacao());
			dto.setDataUltimaLimpeza(agendamento.getDataUltimaLimpeza());
			dto.setMeses(agendamento.getMeses());
			dto.setUserId(agendamento.getUser().getId());

			// Define a próxima limpeza corretamente
			dto.setDataProximaLimpeza(agendamento.getDataUltimaLimpeza().plusMonths(agendamento.getMeses()));

			return dto;
		}).sorted(Comparator.comparing(AgendamentoLimpezaFuturaDTO::getDataProximaLimpeza))
				.collect(Collectors.toList());

		return new PageImpl<>(listaOrdenada, pageable, agendamentos.getTotalElements());
	}

	public AgendamentoLimpeza create(AgendamentoLimpezaDTO dto) {
		// Verifica se o usuário existe
		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

		Cliente cliente = clienteRepository.findById(dto.getClienteId())
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

		// Cria um novo agendamento e define os campos
		AgendamentoLimpeza agendamento = new AgendamentoLimpeza();
		agendamento.setNome(dto.getNome());
		agendamento.setEmail(dto.getEmail());
		agendamento.setEndereco(dto.getEndereco());
		agendamento.setDataInstalacao(dto.getDataInstalacao());
		agendamento.setDataUltimaLimpeza(dto.getDataUltimaLimpeza());
		agendamento.setDataDaProximaLimpeza(dto.getDataUltimaLimpeza().plusMonths(dto.getMeses()));
		agendamento.setMeses(dto.getMeses());
		agendamento.setUser(user);
		agendamento.setCliente(cliente);

		return repository.save(agendamento);
	}

	public AgendamentoLimpeza update(Long id, AgendamentoLimpezaDTO dto) {
		// Verifica se o agendamento existe
		AgendamentoLimpeza agendamento = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado!"));

		// Verifica se o usuário informado existe
		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

		// Atualiza os dados do agendamento
		agendamento.setNome(dto.getNome());
		agendamento.setEmail(dto.getEmail());
		agendamento.setEndereco(dto.getEndereco());
		agendamento.setDataInstalacao(dto.getDataInstalacao());
		agendamento.setDataUltimaLimpeza(dto.getDataUltimaLimpeza());
		agendamento.setDataDaProximaLimpeza(dto.getDataUltimaLimpeza().plusMonths(dto.getMeses()));
		agendamento.setMeses(dto.getMeses());
		agendamento.setUser(user);
		agendamento.setCliente(agendamento.getCliente());

		return repository.save(agendamento);
	}

	public void delete(Long id) {
		// Verifica se o agendamento existe antes de deletar
		AgendamentoLimpeza agendamento = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado!"));
		repository.delete(agendamento);
	}
}
