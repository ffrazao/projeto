package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum MeioContatoFinalidade {

	C("Comercial", 1), P("Particular", 2);

	private String descricao;

	private Integer ordem;

	private MeioContatoFinalidade(String descricao, Integer ordem) {
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
