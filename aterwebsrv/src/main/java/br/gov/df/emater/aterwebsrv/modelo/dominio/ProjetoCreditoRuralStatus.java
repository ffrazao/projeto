package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ProjetoCreditoRuralStatus {

	CA("", 1), CO("", 1), EA("", 1), EE("", 1), LI("", 1), NC("", 1), NE("", 1), PR("", 1);

	private String descricao;

	private Integer ordem;

	private ProjetoCreditoRuralStatus(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}