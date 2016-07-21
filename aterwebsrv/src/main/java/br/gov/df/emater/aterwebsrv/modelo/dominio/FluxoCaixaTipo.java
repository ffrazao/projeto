package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum FluxoCaixaTipo {

	R("Receita", 1), D("Despesa", 2);

	private String descricao;

	private Integer ordem;

	private FluxoCaixaTipo(String descricao, Integer ordem) {
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
