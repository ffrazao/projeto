package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralPeriodicidade;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerDataHora;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerDataHora;

@Entity
@Table(name = "projeto_credito_cronograma_pagamento", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralCronogramaPagamento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "projetoCreditoRuralCronogramaPagamento")
	private List<CronogramaPagamento> cronogramaPagamentoList;

	@Column(name = "data_calculo")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerDataHora.class)
	@JsonDeserialize(using = JsonDeserializerDataHora.class)
	private Calendar dataCalculo;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "data_contratacao")
	private Calendar dataContratacao;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "data_final_carencia")
	private Calendar dataFinalCarencia;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "data_primeira_parcela")
	private Calendar dataPrimeiraParcela;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome_lote")
	private String nomeLote;

	@Column(name = "periodicidade")
	@Enumerated(EnumType.STRING)
	private ProjetoCreditoRuralPeriodicidade periodicidade;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Column(name = "quantidade_parcela")
	private Integer quantidadeParcelas;

	@Enumerated(EnumType.STRING)
	private Confirmacao selecionado;

	@Column(name = "taxa_juros_anual")
	private BigDecimal taxaJurosAnual;

	@Enumerated(EnumType.STRING)
	private FinanciamentoTipo tipo;

	@Column(name = "valor_financiamento")
	private BigDecimal valorFinanciamento;

	@Column(name = "valor_total_juros")
	private BigDecimal valorTotalJuros;

	@Column(name = "valor_total_prestacoes")
	private BigDecimal valorTotalPrestacoes;

	public ProjetoCreditoRuralCronogramaPagamento() {
		super();
	}

	public ProjetoCreditoRuralCronogramaPagamento(Integer id) {
		super(id);
	}

	public List<CronogramaPagamento> getCronogramaPagamentoList() {
		return cronogramaPagamentoList;
	}

	public Calendar getDataCalculo() {
		return dataCalculo;
	}

	public Calendar getDataContratacao() {
		return dataContratacao;
	}

	public Calendar getDataFinalCarencia() {
		return dataFinalCarencia;
	}

	public Calendar getDataPrimeiraParcela() {
		return dataPrimeiraParcela;
	}

	public Integer getId() {
		return id;
	}

	public String getNomeLote() {
		return nomeLote;
	}

	public ProjetoCreditoRuralPeriodicidade getPeriodicidade() {
		return periodicidade;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public Confirmacao getSelecionado() {
		return selecionado;
	}

	public BigDecimal getTaxaJurosAnual() {
		return taxaJurosAnual;
	}

	public FinanciamentoTipo getTipo() {
		return tipo;
	}

	public BigDecimal getValorFinanciamento() {
		return valorFinanciamento;
	}

	public BigDecimal getValorTotalJuros() {
		return valorTotalJuros;
	}

	public BigDecimal getValorTotalPrestacoes() {
		return valorTotalPrestacoes;
	}

	public void setCronogramaPagamentoList(List<CronogramaPagamento> cronogramaPagamentoList) {
		this.cronogramaPagamentoList = cronogramaPagamentoList;
	}

	public void setDataCalculo(Calendar dataCalculo) {
		this.dataCalculo = dataCalculo;
	}

	public void setDataContratacao(Calendar dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public void setDataFinalCarencia(Calendar dataFinalCarencia) {
		this.dataFinalCarencia = dataFinalCarencia;
	}

	public void setDataPrimeiraParcela(Calendar dataPrimeiraParcela) {
		this.dataPrimeiraParcela = dataPrimeiraParcela;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNomeLote(String nomeLote) {
		this.nomeLote = nomeLote;
	}

	public void setPeriodicidade(ProjetoCreditoRuralPeriodicidade periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public void setSelecionado(Confirmacao selecionado) {
		this.selecionado = selecionado;
	}

	public void setTaxaJurosAnual(BigDecimal taxaJurosAnual) {
		this.taxaJurosAnual = taxaJurosAnual;
	}

	public void setTipo(FinanciamentoTipo tipo) {
		this.tipo = tipo;
	}

	public void setValorFinanciamento(BigDecimal valorFinanciamento) {
		this.valorFinanciamento = valorFinanciamento;
	}

	public void setValorTotalJuros(BigDecimal valorTotalJuros) {
		this.valorTotalJuros = valorTotalJuros;
	}

	public void setValorTotalPrestacoes(BigDecimal valorTotalPrestacoes) {
		this.valorTotalPrestacoes = valorTotalPrestacoes;
	}

}
