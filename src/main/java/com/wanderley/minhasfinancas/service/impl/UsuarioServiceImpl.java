package com.wanderley.minhasfinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanderley.minhasfinancas.excetion.ErroAutenticacao;
import com.wanderley.minhasfinancas.excetion.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.model.repository.UsuarioRepository;
import com.wanderley.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {

		Optional<Usuario> usuario =  repository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado.");
		}
		
		if (!usuario.get().getSenha().equals(senha)){
			throw new ErroAutenticacao("Senha inválida.");
			
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		 
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		
		boolean existe = repository.existsByEmail(email);
		
		if (existe) {
			
		   throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");	
			
		}
			
	}

}
