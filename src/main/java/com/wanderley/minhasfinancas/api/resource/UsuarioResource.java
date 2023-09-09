package com.wanderley.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wanderley.minhasfinancas.api.dto.UsuarioDTO;
import com.wanderley.minhasfinancas.exception.ErroAutenticacao;
import com.wanderley.minhasfinancas.exception.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		
		try {
		   Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
		   return ResponseEntity.ok(usuarioAutenticado);
		}catch(ErroAutenticacao erro) {
			return ResponseEntity.badRequest().body(erro.getMessage());
		}
		
	}
	
	@PostMapping
	public ResponseEntity salvar (@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder().nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			Usuario usuarioSalvo =  service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo,HttpStatus.CREATED);
		}catch(RegraNegocioException erro) {
			return ResponseEntity.badRequest().body(erro.getMessage());
		}
				
	}
	
}
