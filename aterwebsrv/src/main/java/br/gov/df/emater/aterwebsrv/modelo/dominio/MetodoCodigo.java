package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum MetodoCodigo {

	PROJETO_CREDITO_RURAL("Elaboração de Projeto de Crédito Rural");

	private String descricao;

	private MetodoCodigo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
