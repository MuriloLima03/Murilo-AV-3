package com.autobots.automanager.excecoes;

import java.time.LocalDateTime;

public class ApiErro {
	private int status;
	private String mensagem;
	private LocalDateTime timestamp;
	private String caminho;
	
	public ApiErro(int status, String mensagem, String caminho) {
		this.status = status;
		this.mensagem = mensagem;
		this.timestamp = LocalDateTime.now();
		this.caminho = caminho;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getCaminho() {
		return caminho;
	}
	
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
}
