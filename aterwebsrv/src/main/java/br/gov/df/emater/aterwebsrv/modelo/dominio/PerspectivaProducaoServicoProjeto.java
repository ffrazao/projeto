package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoServicoProjeto {

	AGROINDUSTRIA("Agro-Ind�stria"), ARTESANATO("Artesanato"), PROC_ARTESANAL("Processamento Artesanal");

	private String descricao;

	private PerspectivaProducaoServicoProjeto(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
