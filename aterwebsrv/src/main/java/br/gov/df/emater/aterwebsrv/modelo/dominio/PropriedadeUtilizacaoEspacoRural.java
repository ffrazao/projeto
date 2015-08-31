package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PropriedadeUtilizacaoEspacoRural {

	LA("Lazer", 1), MO("Moradia", 2), OU("Outra", 7), PA(
			"Preservação Ambiental", 3), PR("Produção", 4), PS(
			"Preservação de Serviço", 5), TR("Turismo Rural", 6);

	private String descricao;

	private Integer ordem;

	private PropriedadeUtilizacaoEspacoRural(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
