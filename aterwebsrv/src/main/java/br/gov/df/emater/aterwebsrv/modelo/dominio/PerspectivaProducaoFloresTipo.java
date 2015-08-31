package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoFloresTipo {

	FLORES("Flores"), FRUTIFERAS("Frut�feras"), GRANDES_CULTURAS("Grandes Culturas"), HORTALICAS("Hortali�as"), ORNAMENTAIS("Ornamentais"), SILVICULTURA("Silvicultura");

	private String descricao;

	private PerspectivaProducaoFloresTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}