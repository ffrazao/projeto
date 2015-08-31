package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum GestaoTipo {

	N("N�o"), S("Sim"), T("Tempor�rio");

	private String descricao;

	private GestaoTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
