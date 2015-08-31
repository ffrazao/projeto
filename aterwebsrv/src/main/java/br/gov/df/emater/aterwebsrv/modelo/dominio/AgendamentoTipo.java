package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AgendamentoTipo {

	_0("N�o repetir, � um evento �nico"), 
	_1("Di�riamente"), 
	_2("Todos os dias �teis (Segunda � Sexta-Feira)"), 
	_3("Semanalmente (a cada {0})"), // usar: DiaSemana.value(?)
	_4("A cada 2 semanas ({0})"), // usar: DiaSemana.value(?)
	_5("Mensalmente (cada �ltimo(a) {0})"), // usar: DiaSemana.value(?)
	_6("Mensalmente (todo dia {0})"), // usar: DiaMes.value(?)
	_7("Anualmente (todo dia {0} de {1})"); // usar: DiaMes.value(?) e Mes.value(?)

	private String descricao;

	private AgendamentoTipo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}