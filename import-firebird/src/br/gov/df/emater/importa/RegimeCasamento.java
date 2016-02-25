package br.gov.df.emater.importa;

import java.util.ArrayList;
import java.util.List;

public class RegimeCasamento {

	private List<String> sisater = new ArrayList<String>();
	private List<String> aterwebid = new ArrayList<String>();
	
	public RegimeCasamento(){
		this.sisater.add("null");					this.aterwebid.add(null);		
		this.sisater.add("Comunhão parcial"); 		this.aterwebid.add("P");
		this.sisater.add("Comunhão total"); 		this.aterwebid.add("U");
		this.sisater.add("Separação de bens"); 		this.aterwebid.add("S");

	/*
		P Comunhão Parcial de Bens
		U Comunhão Universal de Bens
		S Separação Total de Bens
		A Participação Final nos Aquestos	
	*/
	}

	public String getSisater( String val ){
		Integer pos = this.sisater.indexOf(val);
		return aterwebid.get(pos == -1 ? 0 : pos);		
	}	
}
