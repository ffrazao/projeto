package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoFloresProtecaoEpocaForma {

	SAFRA1("Primeira Safra"), SAFRAPD1("Primeira Safra + PD"), SAFRA2("Segunda Safra"), SAFRAPD2("Segunda Safra + PD"), SAFRA3("Terceira Safra"), SAFRAPD3("Terceira Safra + PD"), CAMPO("Campo"), FORMACAO(
			"Forma��o"), PRODUCAO("Produ��o"), PROTEGIDO("Protegido");

	private String descricao;

	private PerspectivaProducaoFloresProtecaoEpocaForma(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}