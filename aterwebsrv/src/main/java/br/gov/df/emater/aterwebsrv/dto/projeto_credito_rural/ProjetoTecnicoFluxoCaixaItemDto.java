package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;

public class ProjetoTecnicoFluxoCaixaItemDto implements Dto {

	private static final long serialVersionUID = 1L;

	private BigDecimal ano01;

	private BigDecimal ano02;

	private BigDecimal ano03;

	private BigDecimal ano04;

	private BigDecimal ano05;

	private BigDecimal ano06;

	private BigDecimal ano07;

	private BigDecimal ano08;

	private BigDecimal ano09;

	private BigDecimal ano10;

	private FluxoCaixaCodigo codigo;
	
	private FluxoCaixaTipo tipo;
	
	

	public BigDecimal getAno01() {
		return ano01;
	}

	public BigDecimal getAno02() {
		return ano02;
	}

	public BigDecimal getAno03() {
		return ano03;
	}

	public BigDecimal getAno04() {
		return ano04;
	}

	public BigDecimal getAno05() {
		return ano05;
	}

	public BigDecimal getAno06() {
		return ano06;
	}

	public BigDecimal getAno07() {
		return ano07;
	}

	public BigDecimal getAno08() {
		return ano08;
	}

	public BigDecimal getAno09() {
		return ano09;
	}

	public BigDecimal getAno10() {
		return ano10;
	}

	public FluxoCaixaCodigo getCodigo() {
		return codigo;
	}

	public void setAno01(BigDecimal ano01) {
		this.ano01 = ano01;
	}

	public void setAno02(BigDecimal ano02) {
		this.ano02 = ano02;
	}

	public void setAno03(BigDecimal ano03) {
		this.ano03 = ano03;
	}

	public void setAno04(BigDecimal ano04) {
		this.ano04 = ano04;
	}

	public void setAno05(BigDecimal ano05) {
		this.ano05 = ano05;
	}

	public void setAno06(BigDecimal ano06) {
		this.ano06 = ano06;
	}

	public void setAno07(BigDecimal ano07) {
		this.ano07 = ano07;
	}

	public void setAno08(BigDecimal ano08) {
		this.ano08 = ano08;
	}

	public void setAno09(BigDecimal ano09) {
		this.ano09 = ano09;
	}

	public void setAno10(BigDecimal ano10) {
		this.ano10 = ano10;
	}

	public void setCodigo(FluxoCaixaCodigo codigo) {
		this.codigo = codigo;
	}

	public FluxoCaixaTipo getTipo() {
		return tipo;
	}

	public void setTipo(FluxoCaixaTipo tipo) {
		this.tipo = tipo;
	}

}