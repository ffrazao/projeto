package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ElementoTipo {
	array("Conjunto de registros", 1, opcaoFormulario()), cep("CEP - Código de Endereçamento Postal", 2, null), combo_multiplo("Combo Multiplo", 3, opcaoLista()), combo_unico("Combo Único", 4, opcaoLista()), data("Data", 5, null), escolha_multipla("Escolha multipla", 6, opcaoLista()), escolha_multipla_objeto(
			"Escolha Multipla (com Objetos)", 7, opcaoLista()), escolha_unica("Escolha Única", 8, opcaoLista()), memo("Memo", 9, null), numero("Número", 10, opcaoNumero()), objeto("Objeto", 11, opcaoFormulario()), resumo_numero("Resumo Numérico", 12, opcaoFormulario()), string("Letras e Números", 13, null);

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
		StringBuilder texto = new StringBuilder();
        texto.append("[" ).append("\n");
        texto.append("    {" ).append("\n");
        texto.append("        \"nome\": \"Fração\"," ).append("\n");
        texto.append("        \"codigo\": \"fracao\"," ).append("\n");
        texto.append("        \"tipo\": \"numero\"," ).append("\n");
        texto.append("        \"opcao\": {" ).append("\n");
        texto.append("            \"fracao\": '0'," ).append("\n");
        texto.append("        }," ).append("\n");
        texto.append("    }," ).append("\n");
        texto.append("]" ).append("\n");

		return texto.toString();		
	}
	
	private static String opcaoLista() {
		StringBuilder texto = new StringBuilder();
        texto.append("[" ).append("\n");
        texto.append("    {" ).append("\n");
        texto.append("        \"nome\": \"Código\"," ).append("\n");
        texto.append("        \"codigo\": \"codigo\"," ).append("\n");
        texto.append("        \"tipo\": \"string\"," ).append("\n");
        texto.append("    }," ).append("\n");
        texto.append("    {" ).append("\n");
        texto.append("        \"nome\": \"Descrição\"," ).append("\n");
        texto.append("        \"codigo\": \"descricao\"," ).append("\n");
        texto.append("        \"tipo\": \"string\"," ).append("\n");
        texto.append("    }," ).append("\n");
        texto.append("    {" ).append("\n");
        texto.append("        \"nome\": \"Lista\"," ).append("\n");
        texto.append("        \"codigo\": \"lista\"," ).append("\n");
        texto.append("        \"tipo\": \"memo\"," ).append("\n");
        texto.append("    }," ).append("\n");
        texto.append("]" ).append("\n");

		return texto.toString();		
	}

	private static String opcaoFormulario() {
		StringBuilder texto = new StringBuilder();
/*        texto.append("[" ).append("\n");
        texto.append("    {" ).append("\n");
        texto.append("        nome: 'Código do Formulário'," ).append("\n");
        texto.append("        codigo: 'codigo'," ).append("\n");
        texto.append("        tipo: 'string'," ).append("\n");
        texto.append("    }," ).append("\n");
        texto.append("    {" ).append("\n");
        texto.append("        nome: 'Versão do Formulário'," ).append("\n");
        texto.append("        codigo: 'versao'," ).append("\n");
        texto.append("        tipo: 'numero'," ).append("\n");
        texto.append("        opcao: {" ).append("\n");
        texto.append("            fracao: '0'," ).append("\n");
        texto.append("        }," ).append("\n");
        texto.append("    }," ).append("\n");
        texto.append("]" ).append("\n");*/

		
        texto.append("{" ).append("\n");
        texto.append("    \"nome\": \"Formulario\"," ).append("\n");
        texto.append("    \"codigo\": \"formulario\"," ).append("\n");
        texto.append("    \"tipo\": \"objeto\"," ).append("\n");
        texto.append("    \"opcao\": [" ).append("\n");
        texto.append("        {" ).append("\n");
        texto.append("            \"nome\": \"Código do Formulário\"," ).append("\n");
        texto.append("            \"codigo\": \"codigo\"," ).append("\n");
        texto.append("            \"tipo\": \"string\"" ).append("\n");
        texto.append("        }," ).append("\n");
        texto.append("        {" ).append("\n");
        texto.append("            \"nome\": \"Versão do Formulário\"," ).append("\n");
        texto.append("            \"codigo\": \"versao\"," ).append("\n");
        texto.append("            \"tipo\": \"numero\"," ).append("\n");
        texto.append("            \"opcao\": {" ).append("\n");
        texto.append("                \"fracao\": \"0\"" ).append("\n");
        texto.append("            }" ).append("\n");
        texto.append("        }" ).append("\n");
        texto.append("    ]" ).append("\n");
        texto.append("}" ).append("\n");

		return texto.toString();		
	}

}
