package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadeFormato {

	P("Planejamento"), E("Execucao");

	private String descricao;

	private AtividadeFormato(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
