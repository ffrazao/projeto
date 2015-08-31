package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum DiaSemana {

	_1("Domingo"), _2("Segunda-Feira"), _3("Ter�a-Feira"), _4("Quarta-Feira"), _5("Quinta-Feira"), _6("Sexta-Feira"), _7("S�bado");

	private String descricao;

	private DiaSemana(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}