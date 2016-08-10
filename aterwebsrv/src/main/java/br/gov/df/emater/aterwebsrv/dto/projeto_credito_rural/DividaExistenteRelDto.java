package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.dto.Dto;

public class DividaExistenteRelDto implements Dto {

	private static final long serialVersionUID = 1L;

	private BigDecimal amortizacao;

	private Calendar dataContratacao;

	private Calendar dataVencimento;

	private String especificacao;

	private String finalidade;

	private BigDecimal juroPercAnual;

	private BigDecimal saldoDevedor;

	private BigDecimal valorContratado;

	public DividaExistenteRelDto(String finalidade, String especificacao, Calendar dataContratacao, Calendar dataVencimento, BigDecimal juroPercAnual, BigDecimal valorContratado, BigDecimal amortizacao, BigDecimal saldoDevedor) {
		super();
		this.finalidade = finalidade;
		this.especificacao = especificacao;
		this.dataContratacao = dataContratacao;
		this.dataVencimento = dataVencimento;
		this.juroPercAnual = juroPercAnual;
		this.valorContratado = valorContratado;
		this.amortizacao = amortizacao;
		this.saldoDevedor = saldoDevedor;
	}

	public BigDecimal getAmortizacao() {
		return amortizacao;
	}

	public Calendar getDataContratacao() {
		return dataContratacao;
	}

	public Calendar getDataVencimento() {
		return dataVencimento;
	}

	public String getEspecificacao() {
		return especificacao;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public BigDecimal getJuroPercAnual() {
		return juroPercAnual;
	}

	public BigDecimal getSaldoDevedor() {
		return saldoDevedor;
	}

	public BigDecimal getValorContratado() {
		return valorContratado;
	}

	public void setAmortizacao(BigDecimal amortizacao) {
		this.amortizacao = amortizacao;
	}

	public void setDataContratacao(Calendar dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public void setJuroPercAnual(BigDecimal juroPercAnual) {
		this.juroPercAnual = juroPercAnual;
	}

	public void setSaldoDevedor(BigDecimal saldoDevedor) {
		this.saldoDevedor = saldoDevedor;
	}

	public void setValorContratado(BigDecimal valorContratado) {
		this.valorContratado = valorContratado;
	}

}
