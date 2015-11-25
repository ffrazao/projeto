package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ElementoTipo {
	array("Conjunto de registros", 1, null), cep("CEP - Código de Endereçamento Postal", 2, null), combo_multiplo("Combo Multiplo", 3, null), combo_unico("Combo Único", 4, null), data("Data", 5, null), escolha_multipla("Escolha multipla", 6, null), escolha_multipla_objeto(
			"Escolha Multipla (com Objetos)", 7, null), escolha_unica("Escolha Única", 8, null), memo("Memo", 9, null), numero("Número", 10, opcaoNumero()), objeto("Objeto", 11, null), resumo_numero("Resumo Numérico", 12, null), string("Letras e Números", 13, null);

	private String descricao;

	private Object opcao;

	private Integer ordem;

	private ElementoTipo(String descricao, Integer ordem, Object opcao) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.opcao = opcao;
	}

	public Object getOpcao() {
		return opcao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
	
	private static String opcaoNumero() {
		return "fracao";
	}

}
