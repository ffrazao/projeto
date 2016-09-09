package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum GrupoSocialEscopo {

	E("Empresa", 1), T("Técnico", 3), U("Unidade Organizacional", 2);

	private String descricao;

	private Integer ordem;

	private GrupoSocialEscopo(String descricao, Integer ordem) {
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
