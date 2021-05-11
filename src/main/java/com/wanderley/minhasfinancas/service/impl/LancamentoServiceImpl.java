package com.wanderley.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;
import org.apache.naming.StringManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.wanderley.minhasfinancas.excetion.RegraNegocioException;
import com.wanderley.minhasfinancas.model.entity.Lancamento;
import com.wanderley.minhasfinancas.model.enums.StatusLancamento;
import com.wanderley.minhasfinancas.model.repository.LancamentoRepository;
import com.wanderley.minhasfinancas.service.LancamentoService;

import net.bytebuddy.matcher.StringMatcher;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;
	
	
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
		
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancametno) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(lancametno.getId());
		validar(lancametno);
		return repository.save(lancametno);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(lancamento.getId());		
		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		// TODO Auto-generated method stub
		
       Example example = Example.of(lancamentoFiltro, 
		                            ExampleMatcher.matching().
		                            withIgnoreCase().
		                            withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
   		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		// TODO Auto-generated method stub
		
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		
		if ( (lancamento.getDescricao() == null) || (lancamento.getDescricao().trim().equals("")) ) {
			
			throw new RegraNegocioException("Informe uma descrição válida.");
			
		}
		
		if ( (lancamento.getMes() < 1) || (lancamento.getMes() > 12) || (lancamento.getMes() == null)  ) {
			throw new RegraNegocioException("Informe um mês válido.");
		}
		
		if ( (lancamento.getAno() == null) || (lancamento.getAno().toString().length() != 4) ) {
			throw new RegraNegocioException("Informe um ano válido.");			
		}
		
		if ( (lancamento.getUsuario() == null) ) {
			throw new RegraNegocioException("Informe um usuário.");			
		}
		
		if ( (lancamento.getValor() == null) || (lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) ) {
			throw new RegraNegocioException("Informe um valor válido.");
		}
		
		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de lançamento.");
		}
		
	}
	
}
