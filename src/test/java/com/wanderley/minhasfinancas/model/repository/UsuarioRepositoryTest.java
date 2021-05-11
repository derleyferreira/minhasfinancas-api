package com.wanderley.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wanderley.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExixtenciaDeUmEmail()
	{
	    //cenario
		Usuario usuario = Usuario.builder().nome("Wanderley").email("email@email.com").build();
		entityManager.persist(usuario);
		
		//açao / execução
		
		boolean result = repository.existsByEmail("email@email.com");
		
		//verificacao		
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
	
		boolean result = repository.existsByEmail("email@email.com");
		
		Assertions.assertThat(result).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		Usuario usuario = criarUsuario();
		
		Usuario usuarioSalvo = repository.save(usuario);
		
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail(){
	   
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		Optional<Usuario> result =  repository.findByEmail("email@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}

	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExistirNaBase(){
		   
		Optional<Usuario> result =  repository.findByEmail("email@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}	
	
	public static Usuario criarUsuario() {
		
		return Usuario.builder().
				nome("wanderley").
				email("email@email.com").
				senha("senha").build();
	}
	
}
