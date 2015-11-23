package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum Situacao {

	A("Ativo", 1), I("Inativo", 2);

	private String descricao;

	private Integer ordem;

	private Situacao(String descricao, Integer ordem) {
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
