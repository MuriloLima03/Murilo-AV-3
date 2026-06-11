package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servicos")
public class ServicoControle {
	@Autowired
	private RepositorioServico repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable long id) {
		Optional<Servico> servico = repositorio.findById(id);
		if (servico.isPresent()) {
			return ResponseEntity.ok(servico.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		if (servicos.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(servicos);
		}
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Servico>> obterServicosPorNome(@PathVariable String nome) {
		List<Servico> servicos = repositorio.findByNome(nome);
		if (servicos.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(servicos);
		}
	}

	@PostMapping
	public ResponseEntity<Servico> cadastrarServico(@RequestBody Servico servico) {
		if (servico.getId() != null && repositorio.existsById(servico.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Servico servicoSalvo = repositorio.save(servico);
		return ResponseEntity.status(HttpStatus.CREATED).body(servicoSalvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Servico> atualizarServico(@PathVariable long id, @RequestBody Servico atualizacao) {
		Optional<Servico> servicoExistente = repositorio.findById(id);
		if (servicoExistente.isPresent()) {
			Servico servico = servicoExistente.get();
			if (atualizacao.getNome() != null) {
				servico.setNome(atualizacao.getNome());
			}
			if (atualizacao.getDescricao() != null) {
				servico.setDescricao(atualizacao.getDescricao());
			}
			if (atualizacao.getValor() > 0) {
				servico.setValor(atualizacao.getValor());
			}
			Servico servicoAtualizado = repositorio.save(servico);
			return ResponseEntity.ok(servicoAtualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirServico(@PathVariable long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
