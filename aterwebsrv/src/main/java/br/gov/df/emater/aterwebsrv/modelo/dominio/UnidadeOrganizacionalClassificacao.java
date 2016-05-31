package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum UnidadeOrganizacionalClassificacao {

	AD("Administração", 1), AP("Apoio", 4), OP("Operacional", 3), TA("Tática", 2);

	private String descricao;

	private Integer ordem;

	private UnidadeOrganizacionalClassificacao(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	};
}
