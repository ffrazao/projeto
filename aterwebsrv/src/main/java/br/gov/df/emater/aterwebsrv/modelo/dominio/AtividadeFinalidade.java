package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadeFinalidade {

	A("Administrativa"), O("Operacional");

	private String descricao;

	private AtividadeFinalidade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
