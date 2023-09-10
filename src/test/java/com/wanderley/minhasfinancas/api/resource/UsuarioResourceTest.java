package com.wanderley.minhasfinancas.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanderley.minhasfinancas.api.dto.UsuarioDTO;
import com.wanderley.minhasfinancas.exception.ErroAutenticacao;
import com.wanderley.minhasfinancas.exception.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.service.LancamentoService;
import com.wanderley.minhasfinancas.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		String email = "email@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Usuario u = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenReturn(u);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
		     .accept(JSON)
		     .contentType(JSON)
		     .content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isOk())
		   .andExpect(MockMvcResultMatchers.jsonPath("id").value(u.getId()))
		   .andExpect(MockMvcResultMatchers.jsonPath("nome").value(u.getNome()))
		   .andExpect(MockMvcResultMatchers.jsonPath("email").value(u.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoRetornarErroDeAutenticacao() throws Exception {
		String email = "email@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
			
		
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
		     .accept(JSON)
		     .contentType(JSON)
		     .content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isBadRequest());
		   
	}	
	
	@Test
	public void deveCriarUmUsuario() throws Exception {
		String email = "email@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Usuario u = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(u);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
		     .accept(JSON)
		     .contentType(JSON)
		     .content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isCreated())
		   .andExpect(MockMvcResultMatchers.jsonPath("id").value(u.getId()))
		   .andExpect(MockMvcResultMatchers.jsonPath("nome").value(u.getNome()))
		   .andExpect(MockMvcResultMatchers.jsonPath("email").value(u.getEmail()));
	}	
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
		String email = "email@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Usuario u = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class)))
		  .thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
		     .accept(JSON)
		     .contentType(JSON)
		     .content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}		
	
}
