package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AssuntoAcaoFinalidade {

	A("Administrativa"), O("Operacional");

	private String descricao;

	private AssuntoAcaoFinalidade(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
