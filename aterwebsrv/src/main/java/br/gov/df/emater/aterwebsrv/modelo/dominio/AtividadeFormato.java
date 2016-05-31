package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadeFormato {

	E("Execução"), P("Planejamento");

	private String descricao;

	private AtividadeFormato(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
