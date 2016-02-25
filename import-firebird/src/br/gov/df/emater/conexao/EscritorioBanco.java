package br.gov.df.emater.conexao;

import java.util.ArrayList;
import java.util.List;

public class EscritorioBanco {

	private List<String> escritorio = new ArrayList<String>();
	private List<String> aterwebid = new ArrayList<String>();
	private List<String> assentamento = new ArrayList<String>();
	
	public EscritorioBanco(){
		this.escritorio.add("ASBRA");this.aterwebid.add("01");this.assentamento.add("S");
		this.escritorio.add("ASCRI");this.aterwebid.add("43");this.assentamento.add("S");
		this.escritorio.add("ASFOR");this.aterwebid.add("63");this.assentamento.add("S");
		this.escritorio.add("ASPBE");this.aterwebid.add("64");this.assentamento.add("S");
		this.escritorio.add("ASPIP");this.aterwebid.add("23");this.assentamento.add("S");
		this.escritorio.add("ASSOB");this.aterwebid.add("15");this.assentamento.add("S");
		this.escritorio.add("ELALG");this.aterwebid.add("02");this.assentamento.add("N");
		this.escritorio.add("ELBRA");this.aterwebid.add("01");this.assentamento.add("N");
		this.escritorio.add("ELBSB");this.aterwebid.add("09");this.assentamento.add("N");
		this.escritorio.add("ELCEI");this.aterwebid.add("13");this.assentamento.add("N");
		this.escritorio.add("ELGAM");this.aterwebid.add("31");this.assentamento.add("N");
		this.escritorio.add("ELJAR");this.aterwebid.add("44");this.assentamento.add("N");
		this.escritorio.add("ELPAD");this.aterwebid.add("22");this.assentamento.add("N");
		this.escritorio.add("ELPAR");this.aterwebid.add("40");this.assentamento.add("N");
		this.escritorio.add("ELPIP");this.aterwebid.add("48");this.assentamento.add("N");
		this.escritorio.add("ELPLA");this.aterwebid.add("19");this.assentamento.add("N");
		this.escritorio.add("ELRIP");this.aterwebid.add("52");this.assentamento.add("N");
		this.escritorio.add("ELRSS");this.aterwebid.add("16");this.assentamento.add("N");
		this.escritorio.add("ELSOB");this.aterwebid.add("15");this.assentamento.add("N");
		this.escritorio.add("ELTAB");this.aterwebid.add("47");this.assentamento.add("N");
		this.escritorio.add("ELTAQ");this.aterwebid.add("53");this.assentamento.add("N");
		this.escritorio.add("ELVAR");this.aterwebid.add("20");this.assentamento.add("N");
	}
	
	public String getEscritorio( int pos){
		return escritorio.get(pos);
	}	
	public String getAterwebId( int pos){
		return aterwebid.get(pos);
	}
	public String getAssentamento( int pos){
		return assentamento.get(pos);
	}
	
}
