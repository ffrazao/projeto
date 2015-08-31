package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadePrioridade {
	
	B("Baixa"), N("Normal"), A("Alta");
	
	private String descricao;

	private AtividadePrioridade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}