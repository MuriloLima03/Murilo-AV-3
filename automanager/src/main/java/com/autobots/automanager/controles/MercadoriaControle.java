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

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControle {
	@Autowired
	private RepositorioMercadoria repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
		Optional<Mercadoria> mercadoria = repositorio.findById(id);
		if (mercadoria.isPresent()) {
			return ResponseEntity.ok(mercadoria.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorio.findAll();
		if (mercadorias.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(mercadorias);
		}
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Mercadoria>> obterMercadoriasPorNome(@PathVariable String nome) {
		List<Mercadoria> mercadorias = repositorio.findByNome(nome);
		if (mercadorias.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(mercadorias);
		}
	}

	@PostMapping
	public ResponseEntity<Mercadoria> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		if (mercadoria.getId() != null && repositorio.existsById(mercadoria.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Mercadoria mercadoriaSalva = repositorio.save(mercadoria);
		return ResponseEntity.status(HttpStatus.CREATED).body(mercadoriaSalva);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Mercadoria> atualizarMercadoria(@PathVariable long id, @RequestBody Mercadoria atualizacao) {
		Optional<Mercadoria> mercadoriaExistente = repositorio.findById(id);
		if (mercadoriaExistente.isPresent()) {
			Mercadoria mercadoria = mercadoriaExistente.get();
			if (atualizacao.getNome() != null) {
				mercadoria.setNome(atualizacao.getNome());
			}
			if (atualizacao.getDescricao() != null) {
				mercadoria.setDescricao(atualizacao.getDescricao());
			}
			if (atualizacao.getValor() > 0) {
				mercadoria.setValor(atualizacao.getValor());
			}
			if (atualizacao.getQuantidade() > 0) {
				mercadoria.setQuantidade(atualizacao.getQuantidade());
			}
			if (atualizacao.getValidade() != null) {
				mercadoria.setValidade(atualizacao.getValidade());
			}
			Mercadoria mercadoriaAtualizada = repositorio.save(mercadoria);
			return ResponseEntity.ok(mercadoriaAtualizada);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirMercadoria(@PathVariable long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
