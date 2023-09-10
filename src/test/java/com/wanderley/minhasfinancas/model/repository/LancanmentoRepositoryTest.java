package com.wanderley.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wanderley.minhasfinancas.model.entity.Lancamento;
import com.wanderley.minhasfinancas.model.enums.StatusLancamento;
import com.wanderley.minhasfinancas.model.enums.TipoLancamento;
import com.wanderley.minhasfinancas.service.LancamentoService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancanmentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		
		Lancamento l = criarLancamento();
		
		Lancamento save = repository.save(l);
		Assertions.assertThat(save.getId()).isNotNull();
		
	}

	public static Lancamento criarLancamento() {
		return Lancamento.builder()
				         .ano(2023)
				         .mes(1)
				         .descricao("lancamento qualquer")
				         .valor(BigDecimal.valueOf(10))
				         .tipo(TipoLancamento.RECEITA)
				         .status(StatusLancamento.PENDENTE)
				         .dataCadastro(LocalDate.now())				         
				         .build();
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento l = criarLancamento();
		
		l = entityManager.persist(l);
		
		l = entityManager.find(Lancamento.class, l.getId());
		
		repository.delete(l);
		
		Lancamento lancamentoApagado = entityManager.find(Lancamento.class, l.getId());
		
		Assertions.assertThat(lancamentoApagado).isNull();
				
	}
	
	@Test
	public void deveAtualizarUmLancamento()
	{
		Lancamento l = criarLancamento();
		entityManager.persist(l);
		
		l.setAno(2021);
		l.setDescricao("teste alteração");
		l.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(l);
		
		Lancamento lancAtualizado  = entityManager.find(Lancamento.class, l.getId());
		
		Assertions.assertThat(lancAtualizado.getAno()).isEqualTo(l.getAno());
		Assertions.assertThat(lancAtualizado.getDescricao()).isEqualTo(l.getDescricao());
		Assertions.assertThat(lancAtualizado.getStatus()).isEqualTo(l.getStatus());
		
	}
	
	@Test
	public void deveBuscarUmLancamentoPorID() {
		Lancamento l = criarLancamento();
		entityManager.persist(l);
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(l.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
		
	}
	
}
