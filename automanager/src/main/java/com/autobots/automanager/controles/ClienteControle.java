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

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuarios")
public class ClienteControle {
	@Autowired
	private RepositorioUsuario repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(usuarios);
		}
	}

	@PostMapping
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
		if (usuario.getId() != null && repositorio.existsById(usuario.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Usuario usuarioSalvo = repositorio.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizarUsuario(@PathVariable long id, @RequestBody Usuario atualizacao) {
		Optional<Usuario> usuarioExistente = repositorio.findById(id);
		if (usuarioExistente.isPresent()) {
			Usuario usuario = usuarioExistente.get();
			if (atualizacao.getNome() != null) {
				usuario.setNome(atualizacao.getNome());
			}
			if (atualizacao.getNomeSocial() != null) {
				usuario.setNomeSocial(atualizacao.getNomeSocial());
			}
			if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
				usuario.setPerfis(atualizacao.getPerfis());
			}
			if (atualizacao.getEmpresa() != null) {
				usuario.setEmpresa(atualizacao.getEmpresa());
			}
			Usuario usuarioAtualizado = repositorio.save(usuario);
			return ResponseEntity.ok(usuarioAtualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
