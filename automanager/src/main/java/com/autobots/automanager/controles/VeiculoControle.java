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

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculos")
public class VeiculoControle {
	@Autowired
	private RepositorioVeiculo repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
		Optional<Veiculo> veiculo = repositorio.findById(id);
		if (veiculo.isPresent()) {
			return ResponseEntity.ok(veiculo.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		if (veiculos.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(veiculos);
		}
	}

	@GetMapping("/placa/{placa}")
	public ResponseEntity<List<Veiculo>> obterVeiculosPorPlaca(@PathVariable String placa) {
		List<Veiculo> veiculos = repositorio.findByPlaca(placa);
		if (veiculos.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(veiculos);
		}
	}

	@PostMapping
	public ResponseEntity<Veiculo> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		if (veiculo.getId() != null && repositorio.existsById(veiculo.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Veiculo veiculoSalvo = repositorio.save(veiculo);
		return ResponseEntity.status(HttpStatus.CREATED).body(veiculoSalvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Veiculo> atualizarVeiculo(@PathVariable long id, @RequestBody Veiculo atualizacao) {
		Optional<Veiculo> veiculoExistente = repositorio.findById(id);
		if (veiculoExistente.isPresent()) {
			Veiculo veiculo = veiculoExistente.get();
			if (atualizacao.getTipo() != null) {
				veiculo.setTipo(atualizacao.getTipo());
			}
			if (atualizacao.getModelo() != null) {
				veiculo.setModelo(atualizacao.getModelo());
			}
			if (atualizacao.getPlaca() != null) {
				veiculo.setPlaca(atualizacao.getPlaca());
			}
			if (atualizacao.getProprietario() != null) {
				veiculo.setProprietario(atualizacao.getProprietario());
			}
			Veiculo veiculoAtualizado = repositorio.save(veiculo);
			return ResponseEntity.ok(veiculoAtualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirVeiculo(@PathVariable long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
