package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadeNatureza {

	D("Demanda"), O("Oferta");

	private String descricao;

	private AtividadeNatureza(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
