package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadePrioridade {

	A("Alta"), B("Baixa"), N("Normal");

	private String descricao;

	private AtividadePrioridade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}