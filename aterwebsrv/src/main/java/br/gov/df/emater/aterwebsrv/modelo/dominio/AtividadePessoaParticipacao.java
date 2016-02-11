package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadePessoaParticipacao {

	D("Demandante"), E("Executor");

	private String descricao;

	private AtividadePessoaParticipacao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
