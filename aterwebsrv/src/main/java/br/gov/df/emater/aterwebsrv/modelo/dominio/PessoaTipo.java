package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaTipo {

	PF("Pessoa Física"), PJ("Pessoa Jurídica");

	private String descricao;

	private PessoaTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}