package com.autobots.automanager.excecoes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TratadorExcecoes extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ResourceNaoEncontradoException.class)
	public ResponseEntity<?> tratarResourceNaoEncontrado(ResourceNaoEncontradoException ex, WebRequest request) {
		ApiErro erro = new ApiErro(
			HttpStatus.NOT_FOUND.value(),
			ex.getMessage(),
			request.getDescription(false).replace("uri=", "")
		);
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}
	

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> tratarExcecaoGeral(Exception ex, WebRequest request) {
		ApiErro erro = new ApiErro(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"Erro interno do servidor: " + ex.getMessage(),
			request.getDescription(false).replace("uri=", "")
		);
		return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> tratarArgumentoInvalido(IllegalArgumentException ex, WebRequest request) {
		ApiErro erro = new ApiErro(
			HttpStatus.BAD_REQUEST.value(),
			"Argumento inválido: " + ex.getMessage(),
			request.getDescription(false).replace("uri=", "")
		);
		return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
	}
}
