package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ArquivoTipo {

	A("Arquivo", 2), P("Perfil", 1);

	private String descricao;

	private Integer ordem;

	private ArquivoTipo(String descricao, Integer ordem) {
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