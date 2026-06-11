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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/empresas")
public class EmpresaControle {
	@Autowired
	private RepositorioEmpresa repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		Optional<Empresa> empresa = repositorio.findById(id);
		if (empresa.isPresent()) {
			return ResponseEntity.ok(empresa.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		if (empresas.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(empresas);
		}
	}

	@PostMapping
	public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa) {
		if (empresa.getId() != null && repositorio.existsById(empresa.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Empresa empresaSalva = repositorio.save(empresa);
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Empresa> atualizarEmpresa(@PathVariable long id, @RequestBody Empresa atualizacao) {
		Optional<Empresa> empresaExistente = repositorio.findById(id);
		if (empresaExistente.isPresent()) {
			Empresa empresa = empresaExistente.get();
			if (atualizacao.getRazaoSocial() != null) {
				empresa.setRazaoSocial(atualizacao.getRazaoSocial());
			}
			if (atualizacao.getNomeFantasia() != null) {
				empresa.setNomeFantasia(atualizacao.getNomeFantasia());
			}
			if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
				empresa.setTelefones(atualizacao.getTelefones());
			}
			if (atualizacao.getEndereco() != null) {
				empresa.setEndereco(atualizacao.getEndereco());
			}
			Empresa empresaAtualizada = repositorio.save(empresa);
			return ResponseEntity.ok(empresaAtualizada);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirEmpresa(@PathVariable long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
