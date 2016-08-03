package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ProjetoCreditoRuralStatus {

	CA("Cancelado", 6), CO("Contratado", 3), EA("Em análise", 1), EE("Em elaboração", 2), LI("Liquidado", 7), NC("Não contratado", 4), NE("Negado", 5), PR("Pró-Rural", 8);

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