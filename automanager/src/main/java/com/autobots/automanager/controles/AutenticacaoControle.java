package com.autobots.automanager.controles;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.seguranca.EncriptadorSenha;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoControle {
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private EncriptadorSenha encriptador;
	
	@PostMapping("/registrar-credencial")
	public ResponseEntity<?> registrarCredencial(@RequestBody CredencialUsuarioSenha credencialDto) {
		try {
			// Validações
			if (credencialDto.getNomeUsuario() == null || credencialDto.getNomeUsuario().isEmpty()) {
				return ResponseEntity.badRequest().body("Nome de usuário é obrigatório");
			}
			if (credencialDto.getSenha() == null || credencialDto.getSenha().isEmpty()) {
				return ResponseEntity.badRequest().body("Senha é obrigatória");
			}
			
			// Criptografar a senha
			String senhaEncriptada = encriptador.encriptarSenha(credencialDto.getSenha());
			
			// Criar a credencial
			CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
			credencial.setNomeUsuario(credencialDto.getNomeUsuario());
			credencial.setSenha(senhaEncriptada);
			credencial.setCriacao(new Date());
			credencial.setInativo(false);
			
			return ResponseEntity.status(HttpStatus.CREATED).body("Credencial registrada com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao registrar credencial: " + e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			// Validações
			if (loginRequest.getNomeUsuario() == null || loginRequest.getNomeUsuario().isEmpty()) {
				return ResponseEntity.badRequest().body("Nome de usuário é obrigatório");
			}
			if (loginRequest.getSenha() == null || loginRequest.getSenha().isEmpty()) {
				return ResponseEntity.badRequest().body("Senha é obrigatória");
			}
			
			// Buscar usuário por nome
			Usuario usuario = repositorioUsuario.findByNome(loginRequest.getNomeUsuario());
			if (usuario == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
			}
			
			// Verificar credenciais
			Optional<CredencialUsuarioSenha> credencialOpt = usuario.getCredenciais().stream()
					.filter(c -> c instanceof CredencialUsuarioSenha)
					.map(c -> (CredencialUsuarioSenha) c)
					.filter(c -> c.getNomeUsuario().equals(loginRequest.getNomeUsuario()) && !c.isInativo())
					.findFirst();
			
			if (credencialOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
			}
			
			CredencialUsuarioSenha credencial = credencialOpt.get();
			
			// Validar senha
			if (!encriptador.validarSenha(loginRequest.getSenha(), credencial.getSenha())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
			}
			
			// Atualizar último acesso
			credencial.setUltimoAcesso(new Date());
			usuario.getCredenciais().remove(credencial);
			usuario.getCredenciais().add(credencial);
			repositorioUsuario.save(usuario);
			
			return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso", usuario.getId(), usuario.getNome()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao realizar login: " + e.getMessage());
		}
	}
	
	// DTO para requisição de login
	public static class LoginRequest {
		private String nomeUsuario;
		private String senha;
		
		public LoginRequest() {}
		
		public LoginRequest(String nomeUsuario, String senha) {
			this.nomeUsuario = nomeUsuario;
			this.senha = senha;
		}
		
		public String getNomeUsuario() {
			return nomeUsuario;
		}
		
		public void setNomeUsuario(String nomeUsuario) {
			this.nomeUsuario = nomeUsuario;
		}
		
		public String getSenha() {
			return senha;
		}
		
		public void setSenha(String senha) {
			this.senha = senha;
		}
	}
	
	// DTO para resposta de login
	public static class LoginResponse {
		private String mensagem;
		private Long usuarioId;
		private String nomeUsuario;
		
		public LoginResponse(String mensagem, Long usuarioId, String nomeUsuario) {
			this.mensagem = mensagem;
			this.usuarioId = usuarioId;
			this.nomeUsuario = nomeUsuario;
		}
		
		public String getMensagem() {
			return mensagem;
		}
		
		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}
		
		public Long getUsuarioId() {
			return usuarioId;
		}
		
		public void setUsuarioId(Long usuarioId) {
			this.usuarioId = usuarioId;
		}
		
		public String getNomeUsuario() {
			return nomeUsuario;
		}
		
		public void setNomeUsuario(String nomeUsuario) {
			this.nomeUsuario = nomeUsuario;
		}
	}
}
