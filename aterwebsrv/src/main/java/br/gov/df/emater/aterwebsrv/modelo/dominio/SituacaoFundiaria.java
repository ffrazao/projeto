package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum SituacaoFundiaria {

	C("Concess�o de Uso"), E("Escritura Definitiva"), P("Posse");

	private String descricao;

	private SituacaoFundiaria(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}