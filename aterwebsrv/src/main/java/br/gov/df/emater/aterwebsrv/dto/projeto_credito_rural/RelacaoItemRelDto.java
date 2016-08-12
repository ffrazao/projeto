package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;

import br.gov.df.emater.aterwebsrv.dto.Dto;

public class RelacaoItemRelDto implements Dto {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private String descricao1;

	private String descricao2;

	private String descricao3;

	private BigDecimal quantidade;

	private String unidade;

	private BigDecimal valorTotal;

	private BigDecimal valorUnitario;

	public RelacaoItemRelDto(String descricao1, String descricao2, String descricao3, Integer ano, BigDecimal quantidade, BigDecimal valorUnitario) {
		super();
		this.descricao1 = descricao1;
		this.descricao2 = descricao2;
		this.descricao3 = descricao3;
		this.ano = ano;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.valorTotal = this.quantidade.multiply(this.valorUnitario);
	}

	public Integer getAno() {
		return ano;
	}

	public String getDescricao1() {
		return descricao1;
	}

	public String getDescricao2() {
		return descricao2;
	}

	public String getDescricao3() {
		return descricao3;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public String getUnidade() {
		return unidade;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setDescricao1(String descricao1) {
		this.descricao1 = descricao1;
	}

	public void setDescricao2(String descricao2) {
		this.descricao2 = descricao2;
	}

	public void setDescricao3(String descricao3) {
		this.descricao3 = descricao3;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}
