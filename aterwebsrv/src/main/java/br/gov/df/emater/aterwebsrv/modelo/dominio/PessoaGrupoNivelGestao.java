package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaGrupoNivelGestao {

	I("Institucional"), T("T�cnico"), U("por Unidade Organizacional");

	private String descricao;

	private PessoaGrupoNivelGestao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}