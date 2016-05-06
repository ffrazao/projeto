package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum TabelaCboTipo {

	F("Familia"), O("Ocupação"), S("Sinônimo");

	private String descricao;

	private TabelaCboTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}