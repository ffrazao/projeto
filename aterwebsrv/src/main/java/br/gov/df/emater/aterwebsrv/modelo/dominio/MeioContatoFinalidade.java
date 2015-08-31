package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum MeioContatoFinalidade {

	C("Comercial"), R("Residencial");

	private String descricao;

	private MeioContatoFinalidade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
