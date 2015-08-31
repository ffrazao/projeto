package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoServicoProduto {

	EMBUTIDO("Embutidos"), HORTALICA("Hortali�as Minimamente Processadas"), PANIFICADO("Panificados"), POLPA("Polpas e Sucos");

	private String descricao;

	private PerspectivaProducaoServicoProduto(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
