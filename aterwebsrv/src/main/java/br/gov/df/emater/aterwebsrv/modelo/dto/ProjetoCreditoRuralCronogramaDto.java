package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

	private Integer periodicidade;

	private String taxaJurosStr;

	private Integer totalParcelas;

	private String valorPresenteStr;

	public Calendar getDataContratacao() {
		return dataContratacao;
	}

	public Calendar getDataFinalCarencia() {
		return dataFinalCarencia;
	}

	public Integer getPeriodicidade() {
		return periodicidade;
	}

	public String getTaxaJurosStr() {
		return taxaJurosStr;
	}

	public Integer getTotalParcelas() {
		return totalParcelas;
	}

	public String getValorPresenteStr() {
		return valorPresenteStr;
	}

	public void setDataContratacao(Calendar dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public void setDataFinalCarencia(Calendar dataFinalCarencia) {
		this.dataFinalCarencia = dataFinalCarencia;
	}

	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setTaxaJurosStr(String taxaJurosStr) {
		this.taxaJurosStr = taxaJurosStr;
	}

	public void setTotalParcelas(Integer totalParcelas) {
		this.totalParcelas = totalParcelas;
	}

	public void setValorPresenteStr(String valorPresenteStr) {
		this.valorPresenteStr = valorPresenteStr;
	}

}