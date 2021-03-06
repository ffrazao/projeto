package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaTipo {

	GS("Grupo Social", 3), PF("Pessoa Física", 1), PJ("Pessoa Jurídica", 2);

	private String descricao;

	private Integer ordem;

	private PessoaTipo(String descricao, Integer ordem) {
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