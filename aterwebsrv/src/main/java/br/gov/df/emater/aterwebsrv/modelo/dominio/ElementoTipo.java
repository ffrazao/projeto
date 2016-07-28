package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ElementoTipo {
	arquivo("Arquivos anexos", 1, null), array("Conjunto de registros", 2, opcaoFormulario()), cep("CEP - Código de Endereçamento Postal", 3, null), combo_multiplo("Combo Multiplo", 4, opcaoLista()), combo_unico("Combo Único", 5, opcaoLista()), data("Data", 6,
			null), escolha_multipla("Escolha multipla", 7, opcaoLista()), escolha_multipla_objeto("Escolha Multipla (com Objetos)", 8,
					opcaoLista()), escolha_unica("Escolha Única", 9, opcaoLista()), memo("Memo", 10, null), numero("Número", 11, opcaoNumero()), objeto("Objeto", 12, opcaoFormulario()), resumo_numero("Resumo Numérico", 13, opcaoFormulario()), string("Letras e Números", 14, null);

	private static String opcaoFormulario() {
		StringBuilder texto = new StringBuilder();
		texto.append("[{").append("\n");
		texto.append("        \"nome\": \"Formularios\",").append("\n");
		texto.append("        \"codigo\": \"formulario\",").append("\n");
		texto.append("        \"tipo\": \"array\",").append("\n");
		texto.append("        \"opcao\": [{").append("\n");
		texto.append("            \"nome\": \"Código do Formulario\",").append("\n");
		texto.append("            \"codigo\": \"formularioCodigo\",").append("\n");
		texto.append("            \"tipo\": \"string\"").append("\n");
		texto.append("        }, {").append("\n");
		texto.append("            \"nome\": \"Versão do Formulario\",").append("\n");
		texto.append("            \"codigo\": \"formularioVersao\",").append("\n");
		texto.append("            \"tipo\": \"numero\",").append("\n");
		texto.append("            \"opcao\": {").append("\n");
		texto.append("                \"fracao\": \"0\"").append("\n");
		texto.append("            }").append("\n");
		texto.append("        }]").append("\n");
		texto.append("}]").append("\n");
		return texto.toString();
	}

	private static String opcaoLista() {
		StringBuilder texto = new StringBuilder();
		texto.append("[").append("\n");
		texto.append("    {").append("\n");
		texto.append("        \"nome\": \"Código\",").append("\n");
		texto.append("        \"codigo\": \"codigo\",").append("\n");
		texto.append("        \"tipo\": \"string\",").append("\n");
		texto.append("    },").append("\n");
		texto.append("    {").append("\n");
		texto.append("        \"nome\": \"Descrição\",").append("\n");
		texto.append("        \"codigo\": \"descricao\",").append("\n");
		texto.append("        \"tipo\": \"string\",").append("\n");
		texto.append("    },").append("\n");
		texto.append("    {").append("\n");
		texto.append("        \"nome\": \"Função Lista\",").append("\n");
		texto.append("        \"codigo\": \"lista\",").append("\n");
		texto.append("        \"tipo\": \"memo\",").append("\n");
		texto.append("    },").append("\n");
		texto.append("]").append("\n");
		return texto.toString();
	}

	private static String opcaoNumero() {
		StringBuilder texto = new StringBuilder();
		texto.append("[").append("\n");
		texto.append("    {").append("\n");
		texto.append("        \"nome\": \"Fração\",").append("\n");
		texto.append("        \"codigo\": \"fracao\",").append("\n");
		texto.append("        \"tipo\": \"numero\",").append("\n");
		texto.append("        \"opcao\": {").append("\n");
		texto.append("            \"fracao\": '0',").append("\n");
		texto.append("        },").append("\n");
		texto.append("    },").append("\n");
		texto.append("]").append("\n");

		return texto.toString();
	}

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

}
