package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "cronograma_pagamento", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CronogramaPagamento extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CronogramaPagamento> {

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
	@JoinColumn(name = "projeto_credito_rural_cronograma_pagamento_id")
	private ProjetoCreditoRuralCronogramaPagamento projetoCreditoRuralCronogramaPagamento;

	@Column(name = "saldo_devedor_final")
	private BigDecimal saldoDevedorFinal;

	@Column(name = "saldo_devedor_inicial")
	private BigDecimal saldoDevedorInicial;

	@Column(name = "taxa_juros")
	private BigDecimal taxaJuros;

	public CronogramaPagamento() {
		super();
	}

	public CronogramaPagamento(BigDecimal amortizacao, Integer ano, Integer epoca, Integer id, BigDecimal juros, Integer parcela, BigDecimal prestacao, BigDecimal saldoDevedorFinal, BigDecimal saldoDevedorInicial, BigDecimal taxaJuros) {
		super();
		this.amortizacao = amortizacao;
		this.ano = ano;
		this.epoca = epoca;
		this.id = id;
		this.juros = juros;
		this.parcela = parcela;
		this.prestacao = prestacao;
		this.saldoDevedorFinal = saldoDevedorFinal;
		this.saldoDevedorInicial = saldoDevedorInicial;
		this.taxaJuros = taxaJuros;
	}

	public CronogramaPagamento(Integer id) {
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

	public ProjetoCreditoRuralCronogramaPagamento getProjetoCreditoRuralCronogramaPagamento() {
		return projetoCreditoRuralCronogramaPagamento;
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

	@Override
	public CronogramaPagamento infoBasica() {
		return new CronogramaPagamento(this.amortizacao, this.ano, this.epoca, this.id, this.juros, this.parcela, this.prestacao, this.saldoDevedorFinal, this.saldoDevedorInicial, this.taxaJuros);
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

	public void setProjetoCreditoRuralCronogramaPagamento(ProjetoCreditoRuralCronogramaPagamento projetoCreditoRuralCronogramaPagamento) {
		this.projetoCreditoRuralCronogramaPagamento = projetoCreditoRuralCronogramaPagamento;
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

}