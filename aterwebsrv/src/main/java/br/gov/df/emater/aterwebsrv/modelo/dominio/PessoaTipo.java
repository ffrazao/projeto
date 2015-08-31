package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaTipo {

	GS("Grupo Social"), PF("Pessoa F�sica"), PJ("Pessoa Jur�dica");

	private String descricao;

	private PessoaTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}