package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum CustoProducaoTipo {

	I("Insumo", 1), S("Serviço", 2);

	private String descricao;

	private Integer ordem;

	private CustoProducaoTipo(String descricao, Integer ordem) {
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
