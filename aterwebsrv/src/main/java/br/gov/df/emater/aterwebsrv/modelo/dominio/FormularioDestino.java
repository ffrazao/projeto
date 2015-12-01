package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum FormularioDestino {

	PA("Beneficiários", 2), PE("Pessoas", 1), PR("Propriedades Rurais", 3);

	private String descricao;

	private Integer ordem;

	private FormularioDestino(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
