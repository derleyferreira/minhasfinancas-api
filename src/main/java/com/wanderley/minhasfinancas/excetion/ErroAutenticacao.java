package com.wanderley.minhasfinancas.excetion;

public class ErroAutenticacao extends RuntimeException {

	public ErroAutenticacao(String mensagem) {
		super(mensagem);
	}
	
}
