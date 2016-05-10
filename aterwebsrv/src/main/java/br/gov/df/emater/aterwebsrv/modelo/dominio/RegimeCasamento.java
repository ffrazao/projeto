package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum RegimeCasamento {

	N("Não se aplica", 1), P("Comunhão parcial", 2), T("Comunhão total", 3), S("Separação de bens", 4), U("União estável", 5);

	private String descricao;

	private Integer ordem;

	private RegimeCasamento(String descricao, Integer ordem) {
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