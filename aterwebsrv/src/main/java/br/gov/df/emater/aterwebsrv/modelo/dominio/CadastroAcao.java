package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum CadastroAcao {
	A("alterar"), E("excluir"), I("incluir");

	private String descricao;

	private CadastroAcao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
