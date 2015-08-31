package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum TipoCargo {

	F("Fam�lia"), O("Ocupa��o"), S("Sin�nimo");

	private String descricao;

	private TipoCargo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}