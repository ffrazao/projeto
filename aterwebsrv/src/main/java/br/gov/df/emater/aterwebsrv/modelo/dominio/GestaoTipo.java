package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum GestaoTipo {

	N("Não"), S("Sim"), T("Temporário");

	private String descricao;

	private GestaoTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
