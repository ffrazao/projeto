package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class Testes {
	public static String STR( String str ){	
		return str == null ? "" : str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "") ;
	}
	public static void main(String[] args) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT * FROM prop00 "); //where idben='11000608' where idben=11000656   
		String mysql = new String();
		Connection my = new Conexao().cnnMySql();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot = 0;
		int id = 0;
		String rot= "";
		
		for( int i=0; i<22; i++ ){
           
			System.out.println(esc.getEscritorio(i) );
			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			
			
			while( rs.next()){
				
				rot = rs.getString("PPROT")==null ? "" : rs.getString("PPROT");

				if( rot.length() > tot ){
					tot = rot.length();
					System.out.println(tot);
				}
					
			}

				/*
					id = Integer.parseInt(rs.getString("IDBEN"))+1000000000;
					id = esc.getEscritorio(i)!="ASBRA"? id : id+18000000; 
					tot++;					 
					for( int j=1; j<=rs.getMetaData().getColumnCount(); j++ ){
						if( rs.getMetaData().getColumnClassName(j) != "[B" ){	
							System.out.println( rs.getMetaData().getColumnName(j) + " - " + rs.getString(j) );
						}
					}*/

		}
		//}
	}
}
