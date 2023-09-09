package com.wanderley.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.wanderley.minhasfinancas.model.enums.StatusLancamento;
import com.wanderley.minhasfinancas.model.enums.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="lancamento",schema="financas")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column()
	private Long Id;
	
	@Column(name="descricao")
	private String descricao;

	@Column(name="mes")
	private Integer mes;
	
	@Column(name="ano")
	private Integer ano;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@Column(name="valor")
	private BigDecimal valor;
	
	@Column(name="data_cadastro")
	@Convert(converter=Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@Column(name="tipo")
	@Enumerated(value=EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name="status")
	@Enumerated(value=EnumType.STRING)
	private StatusLancamento status;
	
}
