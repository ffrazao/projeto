package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadePessoaParticipacao {

	D("Demandante", 1), E("Executor", 2);

	private String descricao;

	private Integer ordem;

	private AtividadePessoaParticipacao(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
