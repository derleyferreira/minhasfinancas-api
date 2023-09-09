package com.wanderley.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wanderley.minhasfinancas.api.dto.UsuarioDTO;
import com.wanderley.minhasfinancas.exception.ErroAutenticacao;
import com.wanderley.minhasfinancas.exception.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.service.LancamentoService;
import com.wanderley.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	private final UsuarioService service;
	private final LancamentoService lancamentoService;
	
	/*public UsuarioResource(UsuarioService service) {
		this.service = service;
	}*/
	
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
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if (!usuario.isPresent())
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
}
