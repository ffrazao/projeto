package br.gov.df.emater.aterwebsrv.modelo.dominio;

import java.math.BigDecimal;

public enum AreaUtil {

	A(1, null, new BigDecimal("2")), B(2, new BigDecimal("2"), new BigDecimal("4")), C(3, new BigDecimal("4"), new BigDecimal("6")), D(3, new BigDecimal("6"), null);

	private BigDecimal ate;

	private BigDecimal de;

	private String descricao;

	private Integer ordem;

	private AreaUtil(Integer ordem, BigDecimal de, BigDecimal ate) {
		this.ordem = ordem;
		this.de = de;
		this.ate = ate;
		if (this.de == null) {
			this.descricao = String.format("< %sha", this.ate.toString());
		} else if (ate == null) {
			this.descricao = String.format("> %sha", this.de.toString());
		} else {
			this.descricao = String.format("de %sha a %sha", this.de.toString(), this.ate.toString());
		}
	}

	public BigDecimal getAte() {
		return ate;
	}

	public BigDecimal getDe() {
		return de;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}