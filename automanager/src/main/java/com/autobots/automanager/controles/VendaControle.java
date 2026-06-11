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

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/vendas")
public class VendaControle {
	@Autowired
	private RepositorioVenda repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
		Optional<Venda> venda = repositorio.findById(id);
		return venda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Venda>> obterVendas() {
		List<Venda> vendas = repositorio.findAll();
		if (vendas.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(vendas);
		}
	}

	@PostMapping
	public ResponseEntity<Venda> cadastrarVenda(@RequestBody Venda venda) {
		if (venda.getId() != null && repositorio.existsById(venda.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Venda vendaSalva = repositorio.save(venda);
		return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Venda> atualizarVenda(@PathVariable long id, @RequestBody Venda atualizacao) {
		Optional<Venda> vendaExistente = repositorio.findById(id);
		if (vendaExistente.isPresent()) {
			Venda venda = vendaExistente.get();

			if (atualizacao.getCadastro() != null) {
				venda.setCadastro(atualizacao.getCadastro());
			}
			if (atualizacao.getIdentificacao() != null) {
				venda.setIdentificacao(atualizacao.getIdentificacao());
			}

			if (atualizacao.getCliente() != null) {
				venda.setCliente(atualizacao.getCliente());
			}

			if (atualizacao.getFuncionario() != null) {
				venda.setFuncionario(atualizacao.getFuncionario());
			}

			if (atualizacao.getVeiculo() != null) {
				venda.setVeiculo(atualizacao.getVeiculo());
			}

			Venda vendaAtualizada = repositorio.save(venda);

			return ResponseEntity.ok(vendaAtualizada);
		}

		return ResponseEntity.notFound().build();
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirVenda(@PathVariable long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
