package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PendenciaTipo {

	E("Erro", 1), A("Aviso", 2);

	private String descricao;

	private Integer ordem;

	private PendenciaTipo(String descricao, Integer ordem) {
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
