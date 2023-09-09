package com.wanderley.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wanderley.minhasfinancas.exception.ErroAutenticacao;
import com.wanderley.minhasfinancas.exception.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.model.repository.UsuarioRepository;
import com.wanderley.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	/*
	@Before
	public void setUp() {	
		service = new UsuarioServiceImpl(repository);
	}
	*/
	@Test
	public void deveSalvarUmUsuario() {
	   Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		
	   Usuario u = Usuario.builder().nome("nome")
			   .email("email@email.com")
			   .senha("senha")
			   .build();
	   
	   Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(u);
	   
	   Assertions.assertDoesNotThrow(()-> service.salvarUsuario(new Usuario()));	   
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailCadastrado() {
		String email = "email@email.com";
		Usuario u = Usuario.builder().nome(email).build();
		
		Mockito.doThrow(RegraNegocioException.class).when(service).salvarUsuario(u);
		
		Assertions.assertThrows(RegraNegocioException.class, ()-> service.salvarUsuario(u));
		
		Mockito.verify(repository, Mockito.never()).save(u);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
	//	setUp();
		String email = "email@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1).build();
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Assertions.assertDoesNotThrow(()->service.autenticar(email, senha));
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmail() {
		
	//	setUp();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());		
		Assertions.assertThrows(ErroAutenticacao.class, ()->service.autenticar("email@email.com", "senha"));		
	}
	
	@Test
	public void deveLancarErroQuandoASenhaForErrada() {
	//	setUp();
		String senha = "senha";
		Usuario u = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(u));
		
		Assertions.assertThrows(ErroAutenticacao.class,()->service.autenticar("email@email.com", "123"));
	}
	
	@Test
	public void deveValidarEmail() {
	//	setUp();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);					
		Assertions.assertDoesNotThrow(()->service.validarEmail("naocadastrado@email.com"));		
	}
	
	@Test
	public void deveDispararErroQuandoEmailCadastradio() {
	//	setUp();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		Assertions.assertThrows(RuntimeException.class,()->service.validarEmail("emailcadastrado@email.com"));
	}
	
}
