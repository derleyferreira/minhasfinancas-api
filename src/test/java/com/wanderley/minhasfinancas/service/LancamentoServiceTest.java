package com.wanderley.minhasfinancas.service;


import static org.mockito.Mockito.mockitoSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wanderley.minhasfinancas.exception.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Lancamento;
import com.wanderley.minhasfinancas.model.entity.Usuario;
import com.wanderley.minhasfinancas.model.enums.StatusLancamento;
import com.wanderley.minhasfinancas.model.repository.LancamentoRepository;
import com.wanderley.minhasfinancas.model.repository.LancanmentoRepositoryTest;
import com.wanderley.minhasfinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoSalvar = LancanmentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoSalvar);
		
		Lancamento lSalvo = LancanmentoRepositoryTest.criarLancamento();
		lSalvo.setId(1l); //l de long
		Mockito.when(repository.save(lancamentoSalvar)).thenReturn(lSalvo);
		
		Lancamento l = service.salvar(lancamentoSalvar);
		
		Assertions.assertThat(l.getId()).isEqualTo(lSalvo.getId());
		
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoSalvar = LancanmentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoSalvar);;
		
		Assertions.catchThrowableOfType(()-> service.salvar(lancamentoSalvar), RegraNegocioException.class);
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lSalvo = LancanmentoRepositoryTest.criarLancamento();
		lSalvo.setId(1l); //l de long
		
		Mockito.doNothing().when(service).validar(lSalvo);
		

		Mockito.when(repository.save(lSalvo)).thenReturn(lSalvo);
		
		service.atualizar(lSalvo);
		
		Mockito.verify(repository, Mockito.times(1)).save(lSalvo);
		
	}	
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
	
		Lancamento lancamentoSalvar = LancanmentoRepositoryTest.criarLancamento();
		lancamentoSalvar.setUsuario(Usuario.builder().id(5).build());
				
		Assertions.catchThrowableOfType(()-> service.atualizar(lancamentoSalvar), NullPointerException.class);
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
		
	}	
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = LancanmentoRepositoryTest.criarLancamento();
		lancamento.setId(1l); //l de long...
	
		service.deletar(lancamento);
		
		Mockito.verify(repository).delete(lancamento);
		
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancanmentoRepositoryTest.criarLancamento();
		
		Assertions.catchThrowableOfType(()->service.deletar(lancamento), NullPointerException.class);

		Mockito.verify(repository,Mockito.never()).delete(lancamento);
		
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		Lancamento l = LancanmentoRepositoryTest.criarLancamento();
		l.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(l);
		
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		List<Lancamento> resultado = service.buscar(l);
		
		Assertions.assertThat(resultado).isNotEmpty()
		                                .hasSize(1)
		                                .contains(l);
	}
	
	@Test
	public void deveAtualizarStatusDeUmLancamento() {
		
		Lancamento l = LancanmentoRepositoryTest.criarLancamento();
		l.setId(1l);
		l.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(l).when(service).atualizar(l);
		
		service.atualizarStatus(l, novoStatus);
		Mockito.verify(service).atualizar(l);
				
	}
	
	@Test
	public void deveObterUmLancamentoPorID() {
		
		Long id = 1l;
		Lancamento l = LancanmentoRepositoryTest.criarLancamento();
		l.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(l));
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioQuandoLancamentoNaoExiste() {
		Long id = 1l;
		Lancamento l = LancanmentoRepositoryTest.criarLancamento();
		l.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		
		Lancamento l = new Lancamento();
		
		Throwable erro = Assertions.catchThrowable(()->service.validar(l));
		
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida");
		
		l.setDescricao("Teste");

		erro = Assertions.catchThrowable(()->service.validar(l));		
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido");		

		l.setMes(1);
		
		//fazer o mesmo para todas as validações.		
	}
	
}
