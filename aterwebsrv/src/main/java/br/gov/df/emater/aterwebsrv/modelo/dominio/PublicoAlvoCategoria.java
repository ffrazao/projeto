package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PublicoAlvoCategoria {

	E("Empreendedor", 1, PublicoAlvoSegmento.F, PublicoAlvoSegmento.P), H("Habitante", 2, PublicoAlvoSegmento.F, PublicoAlvoSegmento.P), T("Trabalhador", 3, PublicoAlvoSegmento.F);

	private String descricao;

	private Integer ordem;

	private PublicoAlvoSegmento[] publicoAlvoSegmentoList;

	private PublicoAlvoCategoria(String descricao, Integer ordem, PublicoAlvoSegmento... publicoAlvoSegmentoList) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.publicoAlvoSegmentoList = publicoAlvoSegmentoList;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public PublicoAlvoSegmento[] getPublicoAlvoSegmentoList() {
		return this.publicoAlvoSegmentoList;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}