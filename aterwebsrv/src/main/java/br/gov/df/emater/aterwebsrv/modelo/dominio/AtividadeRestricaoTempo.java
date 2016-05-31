package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadeRestricaoTempo {

	ER("Quando esta terminar a referida ser� iniciada"), II("Iniciam juntas"), IT("Iniciam e terminam juntas"), RE("Quando a referida terminar esta ser� iniciada"), TT("Terminam juntas");

	private String descricao;

	private AtividadeRestricaoTempo(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}