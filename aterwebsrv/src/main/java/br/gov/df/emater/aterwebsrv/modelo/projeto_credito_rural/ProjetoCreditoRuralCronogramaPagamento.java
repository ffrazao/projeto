package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;

import javax.persistence.Column;
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

	private BigDecimal amortizacao;

	private Integer ano;

	private Integer epoca;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private BigDecimal juros;

	private Integer parcela;

	private BigDecimal prestacao;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Column(name = "saldo_devedor_final")
	private BigDecimal saldoDevedorFinal;

	@Column(name = "saldo_devedor_inicial")
	private BigDecimal saldoDevedorInicial;

	@Column(name = "taxa_juros")
	private BigDecimal taxaJuros;

	@Enumerated(EnumType.STRING)
	private FinanciamentoTipo tipo;

	public ProjetoCreditoRuralCronogramaPagamento() {
		super();
	}

	public ProjetoCreditoRuralCronogramaPagamento(Integer id) {
		super(id);
	}

	public BigDecimal getAmortizacao() {
		return amortizacao;
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

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public BigDecimal getSaldoDevedorFinal() {
		return saldoDevedorFinal;
	}

	public BigDecimal getSaldoDevedorInicial() {
		return saldoDevedorInicial;
	}

	public BigDecimal getTaxaJuros() {
		return taxaJuros;
	}

	public FinanciamentoTipo getTipo() {
		return tipo;
	}

	public void setAmortizacao(BigDecimal amortizacao) {
		this.amortizacao = amortizacao;
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

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setSaldoDevedorFinal(BigDecimal saldoDevedorFinal) {
		this.saldoDevedorFinal = saldoDevedorFinal;
	}

	public void setSaldoDevedorInicial(BigDecimal saldoDevedorInicial) {
		this.saldoDevedorInicial = saldoDevedorInicial;
	}

	public void setTaxaJuros(BigDecimal taxaJuros) {
		this.taxaJuros = taxaJuros;
	}

	public void setTipo(FinanciamentoTipo tipo) {
		this.tipo = tipo;
	}

}