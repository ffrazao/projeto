package br.gov.df.emater.importa;

import java.util.regex.Pattern;

public class StringExt {

	public static String STR(String str){
		return str == null ? "" : str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "").trim() ;
	}
	public static String MyString( String str ){
		return str == null ? "null" : "\"" + str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "").trim() + "\"" ;
	}
	public static String MyInteger( String str ){
		return str == null ? "null" : str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "").trim() ;
	}
	
	public static String STRFloat( String str ){	
		return (String) (STR(str)!="" ? Float.toString(Float.parseFloat(STR(str))) : "0.0")  ;
	}
	
	public static String Telefone( String tel, String cel ){
		cel=cel.replaceAll("[a-zA-Z]","");
		tel=tel.replaceAll("[a-zA-Z]","");
		if( tel.isEmpty() && cel.isEmpty() ){ return ""; }
		if( tel.isEmpty() ){ return cel; }
		if( cel.isEmpty() ){ return tel; }
		return tel+" - "+cel;	
	}
	
	public static String numTrim( String val ){
		return val.replaceAll("[.-]", "").replaceAll(" ", ""); 
	}

	public static String CEP( String val ){
		if(val=="" || val==null){
			return "";
		} else {
			return formatar(numTrim(val), "#####-###" );
		}
	}

	public static String CPF( String val ){
		return formatar(numTrim(val), "###.###.###-##" );
	}

	public static String CNPJ( String val ){
		return formatar(numTrim(val), "##.###.###/####-##");
	}
	
    public static String formatar( String valor, String mascara ) {
        
        String dado = "";      
        // remove caracteres nao numericos
        for ( int i = 0; i < valor.length(); i++ )  {
            char c = valor.charAt(i);
            if ( Character.isDigit( c ) ) { dado += c; }
        }

        int indMascara = mascara.length();
        int indCampo = dado.length();

        for ( ; indCampo > 0 && indMascara > 0; ) {
            if ( mascara.charAt( --indMascara ) == '#' ) { indCampo--; }
        }

        String saida = "";
        for ( ; indMascara < mascara.length(); indMascara++ ) {    
            saida += ( ( mascara.charAt( indMascara ) == '#' ) ? dado.charAt( indCampo++ ) : mascara.charAt( indMascara ) );
        }    
        return saida;
    }
	

}
