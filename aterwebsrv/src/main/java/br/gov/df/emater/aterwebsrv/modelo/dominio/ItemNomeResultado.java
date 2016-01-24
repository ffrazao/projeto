package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ItemNomeResultado {

	M("Média", 2), N("Nada", 3), S("Somatório", 1);

	private String descricao;

	private Integer ordem;

	private ItemNomeResultado(String descricao, Integer ordem) {
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
