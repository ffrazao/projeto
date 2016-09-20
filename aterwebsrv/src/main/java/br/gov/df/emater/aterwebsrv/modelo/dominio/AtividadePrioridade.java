package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadePrioridade {

	A("Alta"), N("Normal"), B("Baixa");

	private String descricao;

	private AtividadePrioridade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}