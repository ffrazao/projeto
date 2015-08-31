package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum StatusValidoInvalido {

	I("Inv�lido"), V("V�lido");

	private String descricao;

	private StatusValidoInvalido(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}