package com.autobots.automanager.excecoes;

public class ResourceNaoEncontradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ResourceNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public ResourceNaoEncontradoException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
