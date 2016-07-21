package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum FinanciamentoTipo {

	C("Custeio", 2), I("Investimento", 1);

	private String descricao;

	private Integer ordem;

	private FinanciamentoTipo(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
