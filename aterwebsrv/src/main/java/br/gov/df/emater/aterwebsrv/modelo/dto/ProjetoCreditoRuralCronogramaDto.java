package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralPeriodicidade;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class ProjetoCreditoRuralCronogramaDto implements Dto {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataContratacao;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataFinalCarencia;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataPrimeiraParcela;

	private ProjetoCreditoRuralPeriodicidade periodicidade;

	private Integer quantidadeParcelas;

	private BigDecimal taxaJurosAnual;

	private FinanciamentoTipo tipo;

	private BigDecimal valorFinanciamento;

	private BigDecimal valorTotalJuros;

	private BigDecimal valorTotalParcelas;

	public Calendar getDataContratacao() {
		return dataContratacao;
	}

	public Calendar getDataFinalCarencia() {
		return dataFinalCarencia;
	}

	public Calendar getDataPrimeiraParcela() {
		return dataPrimeiraParcela;
	}

	public ProjetoCreditoRuralPeriodicidade getPeriodicidade() {
		return periodicidade;
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public BigDecimal getTaxaJurosAnual() {
		return taxaJurosAnual;
	}

	public FinanciamentoTipo getTipo() {
		return this.tipo;
	}

	public BigDecimal getValorFinanciamento() {
		return valorFinanciamento;
	}

	public BigDecimal getValorTotalJuros() {
		return valorTotalJuros;
	}

	public BigDecimal getValorTotalParcelas() {
		return valorTotalParcelas;
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

	public void setPeriodicidade(ProjetoCreditoRuralPeriodicidade periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
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

	public void setValorTotalParcelas(BigDecimal valorTotalParcelas) {
		this.valorTotalParcelas = valorTotalParcelas;
	}

}