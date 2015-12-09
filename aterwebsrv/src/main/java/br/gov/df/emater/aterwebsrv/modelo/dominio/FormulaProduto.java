package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum FormulaProduto {
	
	A("", 1), B("", 2);

	private String descricao;

	private Integer ordem;

	private FormulaProduto(String descricao, Integer ordem) {
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
