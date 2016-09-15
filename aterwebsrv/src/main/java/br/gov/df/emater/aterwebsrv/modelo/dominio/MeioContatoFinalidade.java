package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum MeioContatoFinalidade {

	C("Para Contato", 1), P("Privativo", 2);

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
