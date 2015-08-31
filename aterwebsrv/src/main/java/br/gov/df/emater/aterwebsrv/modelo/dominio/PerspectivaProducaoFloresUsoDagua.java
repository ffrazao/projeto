package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoFloresUsoDagua {

	IRRIGADO("Irrigado"), SEQUEIRO("Sequeiro");

	private String descricao;

	private PerspectivaProducaoFloresUsoDagua(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}