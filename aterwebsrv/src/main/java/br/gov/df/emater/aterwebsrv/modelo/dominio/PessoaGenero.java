package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaGenero {

	F("Feminino"), M("Masculino");

	private String descricao;

	private PessoaGenero(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}