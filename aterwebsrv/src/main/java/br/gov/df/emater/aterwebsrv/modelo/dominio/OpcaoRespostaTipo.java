package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum OpcaoRespostaTipo {

	A("Assinatura"), I("Informa��o"), M("Objetiva - Multi Valor"), Q("Quebra de P�gina"), S("Subjetiva"), U("Objetiva - �nico Valor");

	private String descricao;

	private OpcaoRespostaTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}