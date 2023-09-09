package com.wanderley.minhasfinancas.service;

import com.wanderley.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
}
