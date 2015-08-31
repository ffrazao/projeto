package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaGeracao {

	A("Adulto"), I("Idoso"), J("Jovem"), C("Criança");

	private String descricao;

	private PessoaGeracao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
