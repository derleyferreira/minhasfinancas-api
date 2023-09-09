package com.wanderley.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanderley.minhasfinancas.exception.ErroAutenticacao;
import com.wanderley.minhasfinancas.exception.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.model.repository.UsuarioRepository;
import com.wanderley.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository repository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		Optional<Usuario> usuario =  repository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
        validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		if (repository.existsByEmail(email)) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}

}
