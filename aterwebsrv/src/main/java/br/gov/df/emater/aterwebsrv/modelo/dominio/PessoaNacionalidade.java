package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaNacionalidade {

	BN("Brasileiro Nato", 1), ES("Estrangeiro", 3), NA("Brasileiro Naturalizado", 2);

	private String descricao;

	private Integer ordem;

	private PessoaNacionalidade(String descricao, Integer ordem) {
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
