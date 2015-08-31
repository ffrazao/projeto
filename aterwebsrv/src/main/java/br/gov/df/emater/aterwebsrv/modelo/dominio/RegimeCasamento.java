package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum RegimeCasamento {

	A("Participa��o Final nos Aquestos", 4), P("Comunh�o Parcial de Bens", 1), S("Separa��o Total de Bens", 3), U("Comunh�o Universal de Bens", 2);

	private String descricao;

	private Integer ordem;

	private RegimeCasamento(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}