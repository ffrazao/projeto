package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum Confirmacao {

	N("Não", 2), S("Sim", 1);

	private String descricao;

	private Integer ordem;

	private Confirmacao(String descricao, Integer ordem) {
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