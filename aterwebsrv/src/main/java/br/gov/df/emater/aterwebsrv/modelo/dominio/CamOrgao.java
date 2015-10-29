package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum CamOrgao {

	A("Aeronáutica"), D("Ministério da Defesa"), E("Exército"), M("Marinha");

	private String descricao;

	private CamOrgao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}