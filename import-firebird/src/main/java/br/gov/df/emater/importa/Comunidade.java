package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.df.emater.conexao.Conexao;

public class Comunidade {

	private static Conexao cn = new Conexao();
	private static Connection my;
	
	public Comunidade() throws SQLException{

		my = cn.cnnMySql();
		 	
	}
	
	public String getComunidade(String und, String com) throws SQLException{
		String res = "";
		com = com == null ? "0" : com.toString();
		PreparedStatement stmCom = my.prepareStatement("select id from ater.comunidade where codigo_sisater = \"" + und + "-" + com +"\"");
		ResultSet rsCom = stmCom.executeQuery(); 
		if( ! rsCom.next() ){
			System.out.println(und + " - " +com);
			res = "289";
		} else {
			res = rsCom.getString("id") ;
		}
		return res;
	}
	
	public String getBacia(String esc, String com) throws SQLException{
		String res = ""; 
		if( com==null ){
			res = "999";
		} else {
			System.out.println(com);
			Connection fb = new Conexao().cnnFireBird(esc );
			ResultSet fbBacia =  fb.prepareStatement("select BACIA from COM00 where IDCOM = " + com + " ;").executeQuery(); fbBacia.next();
			if( ! fbBacia.next() ){ res = "999";} 
			else{ 
				System.out.println(fbBacia.getString("BACIA"));
				ResultSet myBacia =  my.prepareStatement("select id from ater.bacia_hidrografica where nome = \"" + fbBacia.getString("BACIA") + "\" ;").executeQuery(); 
				if( ! myBacia.next() ){ res = "999";} 
				else                  { res = myBacia.getString("id") ; }
			}
			fb.close();
		}
		return res;
	}
	
}
