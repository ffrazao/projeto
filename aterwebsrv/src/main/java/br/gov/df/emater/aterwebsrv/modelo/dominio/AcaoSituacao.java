package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AcaoSituacao {

	P("Planejada mas nao iniciada"), E("Em execu��o"), C("Cancelada"), S("Suspensa ou aguardando");

	private String descricao;

	private AcaoSituacao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}