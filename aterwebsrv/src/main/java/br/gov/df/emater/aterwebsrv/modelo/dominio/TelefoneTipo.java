package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum TelefoneTipo {

	CE("Celular", 1), FA("Fax", 4), FI("Telefone Fixo", 2), TF("Telefone Fixo e Fax", 3);

	private String descricao;

	private Integer ordem;

	private TelefoneTipo(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}