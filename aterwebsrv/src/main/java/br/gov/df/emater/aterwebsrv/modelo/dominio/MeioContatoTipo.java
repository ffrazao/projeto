package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum MeioContatoTipo {

	EMA("Email"), END("Endere�o"), TEL("Telef�nico");

	private String descricao;

	private MeioContatoTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
