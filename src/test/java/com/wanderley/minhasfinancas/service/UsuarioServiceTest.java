package com.wanderley.minhasfinancas.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockitoSession;

import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.assertj.core.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wanderley.minhasfinancas.excetion.ErroAutenticacao;
import com.wanderley.minhasfinancas.excetion.RegraNegocioException;
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
	
	@Before
	public void setUp() {
		
		service = new UsuarioServiceImpl(repository);
	}

	@Test
	public void deveSalvarUmUsuario() {
		
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		
		Usuario usuario = Usuario.builder().id(1l).nome("wanderley").email("email@email.com").senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("wanderley");
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");		
		
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		
		Usuario usuario = Usuario.builder().email("email@email.com").build();
		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail("email@email.com");
		
		Assertions.assertThrows(RegraNegocioException.class, ()->service.salvarUsuario(usuario));
		
		Mockito.verify(repository,Mockito.never()).save(usuario);		
		
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		
		setUp();
		
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1).build();
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
			
		
		Assertions.assertDoesNotThrow(()->service.autenticar(email, senha));
		
		//org.assertj.core.api.Assertions.assertThat(result).isNotNull();;
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		
		setUp();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		Throwable erro = org.assertj.core.api.Assertions.catchThrowable(()->service.autenticar("email@email.com", "senha"));
		
		org.assertj.core.api.Assertions.assertThat(erro).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado.");
		
		//Assertions.assertThrows(ErroAutenticacao.class, ()->service.autenticar("email@email.com", "senha"));
				
	}
	
	@Test
	public void deveLancarErroQuandoSenhaInvalida() {
		
		setUp();
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//Assertions.assertThrows(ErroAutenticacao.class, ()->service.autenticar("email@email.com", "123"));
		
		Throwable erro = org.assertj.core.api.Assertions.catchThrowable(()->service.autenticar("email@email.com", "123"));
		
		org.assertj.core.api.Assertions.assertThat(erro).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
		
	}
		
	@Test
	public void deveValidarEmail() {
		
		setUp();
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		
		Assertions.assertDoesNotThrow(()->service.validarEmail("email@email.com"));
				
	}
	
	@Test
	public void deveLancarErroQuandoExistirEmailCadastrado() {
		
		setUp();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);		
		
		Assertions.assertThrows(RegraNegocioException.class,()->service.validarEmail("email@email.com"));
	}
	
}
