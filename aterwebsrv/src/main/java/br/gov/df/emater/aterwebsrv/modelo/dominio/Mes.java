package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum Mes {

	_01("Janeiro"), _02("Fevereiro"), _03("Mar�o"), _04("Abril"), _05("Maio"), _06("Junho"), _07("Julho"), _08("Agosto"), _09("Setembro"), _10("Outubro"), _11("Novembro"), _12("Dezembro");

	private String descricao;

	private Mes(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}