package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum Nacionalidade {

	BN("Brasileiro Nato"), ES("Estrangeiro"), NA("Brasileiro Naturalizado");

	private String descricao;

	private Nacionalidade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
