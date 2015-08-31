package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum CnhCategoria {

	A("A", 1), AB("AB", 2), B("B", 3), C("C", 4), D("D", 5), E("E", 6);

	private String descricao;

	private Integer ordem;

	private CnhCategoria(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}