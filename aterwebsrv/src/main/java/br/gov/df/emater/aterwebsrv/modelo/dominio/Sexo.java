package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum Sexo {

	F("Feminino"), M("Masculino");

	private String descricao;

	private Sexo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}