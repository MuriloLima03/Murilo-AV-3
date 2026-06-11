package com.autobots.automanager.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncriptadorSenha {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String encriptarSenha(String senha) {
		return passwordEncoder.encode(senha);
	}
	
	public boolean validarSenha(String senhaPlana, String senhaEncriptada) {
		return passwordEncoder.matches(senhaPlana, senhaEncriptada);
	}
}
