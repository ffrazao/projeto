package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PublicoAlvoCategoria {

	E("Empreendedor", 1), H("Habitante", 2), T("Trabalhador", 3);

	private String descricao;

	private Integer ordem;

	private PublicoAlvoCategoria(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}