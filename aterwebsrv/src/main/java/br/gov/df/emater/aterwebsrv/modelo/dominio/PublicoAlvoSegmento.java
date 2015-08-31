package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PublicoAlvoSegmento {

	F("Familiar", 2), P("Patronal", 1);

	private String descricao;

	private Integer ordem;

	private PublicoAlvoSegmento(String descricao, Integer ordem) {
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