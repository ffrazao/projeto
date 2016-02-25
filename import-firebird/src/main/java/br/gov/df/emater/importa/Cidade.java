package br.gov.df.emater.importa;

import java.util.ArrayList;
import java.util.List;

public class Cidade {
	
	private List<String> nome = new ArrayList<String>();
	private List<String> codigo = new ArrayList<String>();
	
	public Cidade(){
		this.nome.add("Brasília");this.codigo.add("1");
		this.nome.add("Água Fria");this.codigo.add("2");
		this.nome.add("Cocalzinho");this.codigo.add("3");
		this.nome.add("Formosa");this.codigo.add("4");
		this.nome.add("Planaltina de Goiás");this.codigo.add("6");
		this.nome.add("Planltina de Goiás");this.codigo.add("6");
		this.nome.add("Cristalina");this.codigo.add("5");
		this.nome.add("São Marcos");this.codigo.add("5");
		this.nome.add("Águas Claras");this.codigo.add("8");	
		this.nome.add("Alexandre Gusmão");this.codigo.add("9");
		this.nome.add("Brazlândia");this.codigo.add("10");
		this.nome.add("Ceilândia");this.codigo.add("11");
		this.nome.add("Gama");this.codigo.add("12");
		this.nome.add("Guará");this.codigo.add("13");
		this.nome.add("Lago Norte");this.codigo.add("14");
		this.nome.add("Núcleo bandeirante");this.codigo.add("15");
		this.nome.add("Padre Bernardo");this.codigo.add("16");
		this.nome.add("Paranoá");this.codigo.add("17");
		this.nome.add("Park Way");this.codigo.add("18");
		this.nome.add("Planaltina");this.codigo.add("19");
		this.nome.add("Recanto das Emas");this.codigo.add("20");
		this.nome.add("Riacho Fundo I");this.codigo.add("21");
		this.nome.add("Riacho Fundo II");this.codigo.add("22");
		this.nome.add("Samambaia");this.codigo.add("23");
		this.nome.add("Santa Maria");this.codigo.add("24");
		this.nome.add("São Marcos");this.codigo.add("25");
		this.nome.add("São Sebastião");this.codigo.add("26");
		this.nome.add("SCIA");this.codigo.add("27");
		this.nome.add("Sobradinho");this.codigo.add("28");
		this.nome.add("Taguatinga");this.codigo.add("29");
																																												
	}

	public String getCodigo(String val) {
		Integer pos = this.nome.indexOf(val);
		pos = pos == -1 ? 0 : pos;
		return codigo.get(pos);				   
	}

}
