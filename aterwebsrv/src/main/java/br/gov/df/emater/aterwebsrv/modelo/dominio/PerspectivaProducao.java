package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducao {

	AGRICOLA("Agr�cola"), AGROINDUSTRIA("Agro-ind�stria"), ANIMAL("Animal"), CORTE("Corte"), FLORES("Flores"), LEITE("Leite"), POSTURA("Postura"), SERVICO("Servi�o"), TURISMO("Turismo");

	private String descricao;

	private PerspectivaProducao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
