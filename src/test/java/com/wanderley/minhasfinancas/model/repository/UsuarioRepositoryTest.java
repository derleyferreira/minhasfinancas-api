package com.wanderley.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
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
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario u = Usuario.builder().nome("Wanderley").email("email@email.com").build();
		entityManager.persist(u);
		
		//ação
		boolean result = repository.existsByEmail("email@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalseQuandoNaoHouverUsuarioCadastradoComEmail() {
				
		//ação
		boolean result = repository.existsByEmail("email@email.com");
		
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		Usuario u = criarUsuario();
		
		Usuario usuarioSalvo = repository.save(u);
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		Usuario u = criarUsuario();
		entityManager.persist(u);
		
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoBuscarUsuarioPorEmailNaoCadastrado() {
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();		
	}
	
	public static Usuario criarUsuario() {
		return Usuario.builder().nome("usuario").email("usuario@email.com")
			    .senha("senha")
			    .build();
	}
	
	
	
}
