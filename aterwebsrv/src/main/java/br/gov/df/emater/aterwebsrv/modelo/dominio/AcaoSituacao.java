package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AcaoSituacao {

	C("Cancelada"), E("Em execu��o"), P("Planejada mas nao iniciada"), S("Suspensa ou aguardando");

	private String descricao;

	private AcaoSituacao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}