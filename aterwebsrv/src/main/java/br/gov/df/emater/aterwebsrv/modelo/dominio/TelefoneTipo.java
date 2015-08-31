package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum TelefoneTipo {

	CE("Celular"), FA("Fax"), FI("Telefone Fixo"), TF("Telefone Fixo e Fax");

	private String descricao;

	private TelefoneTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}