package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ConfirmacaoDap {

	D("Não Dapeável", 3), N("Não", 2), S("Sim", 1);

	private String descricao;

	private Integer ordem;

	private ConfirmacaoDap(String descricao, Integer ordem) {
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