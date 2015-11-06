package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum CargoTipo {

	F("Familia"), O("Ocupação"), S("Sinônimo");

	private String descricao;

	private CargoTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}