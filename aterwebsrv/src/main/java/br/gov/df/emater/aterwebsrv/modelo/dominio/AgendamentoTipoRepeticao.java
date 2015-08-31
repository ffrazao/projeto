package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AgendamentoTipoRepeticao {

	C("Contagem de Vezes"), 
	D("Data T�rmino do Agendamento");

	private String descricao;

	private AgendamentoTipoRepeticao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}