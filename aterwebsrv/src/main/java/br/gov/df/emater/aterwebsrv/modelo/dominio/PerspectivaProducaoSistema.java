package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoSistema {

	CONVENCIONAL("Convencional"), ORGANICO_CERT("Org�nico Certificado");

	private String descricao;

	private PerspectivaProducaoSistema(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}