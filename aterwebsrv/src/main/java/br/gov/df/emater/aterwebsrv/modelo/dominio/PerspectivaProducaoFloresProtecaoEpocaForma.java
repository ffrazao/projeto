package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PerspectivaProducaoFloresProtecaoEpocaForma {

	CAMPO("Campo"), FORMACAO("Formação"), PRODUCAO("Produção"), PROTEGIDO("Protegido"), SAFRA1("Primeira Safra"), SAFRA2("Segunda Safra"), SAFRA3("Terceira Safra"), SAFRAPD1("Primeira Safra + PD"), SAFRAPD2("Segunda Safra + PD"), SAFRAPD3("Terceira Safra + PD");

	private String descricao;

	private PerspectivaProducaoFloresProtecaoEpocaForma(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}