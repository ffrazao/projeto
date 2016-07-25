package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;

@Entity
@Table(name = "cronograma_pagamento", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralCronogramaPagamento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private Integer epoca;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private BigDecimal juros;

	private Integer parcela;

	private BigDecimal prestacao;

	private BigDecimal principal;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCreditoRural;

	private BigDecimal saldo;

	@Enumerated(EnumType.STRING)
	private FinanciamentoTipo tipo;

	public ProjetoCreditoRuralCronogramaPagamento() {
		super();
	}

	public ProjetoCreditoRuralCronogramaPagamento(Integer id) {
		super(id);
	}

	public Integer getAno() {
		return ano;
	}

	public Integer getEpoca() {
		return epoca;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public BigDecimal getJuros() {
		return juros;
	}

	public Integer getParcela() {
		return parcela;
	}

	public BigDecimal getPrestacao() {
		return prestacao;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public FinanciamentoTipo getTipo() {
		return tipo;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setEpoca(Integer epoca) {
		this.epoca = epoca;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setJuros(BigDecimal juros) {
		this.juros = juros;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public void setPrestacao(BigDecimal prestacao) {
		this.prestacao = prestacao;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setTipo(FinanciamentoTipo tipo) {
		this.tipo = tipo;
	}

}