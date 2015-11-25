package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ElementoTipo {
	array("Conjunto de registros", 1), cep("CEP - Código de Endereçamento Postal", 2), combo_multiplo("Combo Multiplo", 3), combo_unico("Combo Único", 4), data("Data", 5), escolha_multipla("Escolha multipla", 6), escolha_multipla_objeto("Escolha Multipla (com Objetos)",
			7), escolha_unica("Escolha Única", 8), memo("Memo", 9), numero("Número", 10), objeto("Objeto", 11), resumo_numero("Resumo Numérico", 12), string("Letras e Números", 13);

	private String descricao;

	private Integer ordem;

	private ElementoTipo(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
