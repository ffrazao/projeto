package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum SituacaoFundiaria {

	C("Concessão de Uso", 3), E("Escritura Definitiva", 1), P("Posse", 2);

	private String descricao;

	private Integer ordem;

	private SituacaoFundiaria(String descricao, Integer ordem) {
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