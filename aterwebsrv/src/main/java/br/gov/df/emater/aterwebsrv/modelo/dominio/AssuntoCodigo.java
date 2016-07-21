package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AssuntoCodigo {

	INVALIDO("Código Inválido");

	private String descricao;

	private AssuntoCodigo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
